package io.endeavour.stocks.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.endeavour.stocks.UnitTestBase;
import io.endeavour.stocks.entity.crud.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.io.IOException;
import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.*;

@AutoConfigureMockMvc
class CrudControllerTest extends UnitTestBase {

    private static final Logger LOGGER = LoggerFactory.getLogger(CrudControllerTest.class);

    /**
     * This object Mapper is used to convert a json String into an object and Vice Versa
     */
    ObjectMapper objectMapper = JsonMapper.builder().addModule(new JavaTimeModule()).build();
    @Autowired
    MockMvc mockMvc;

    ThreadLocal<Person> personThreadLocal = new ThreadLocal<>();

    @Test
    public void getPerson_NotFound() throws Exception {
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/crud/getPerson/?personID=903");

        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }


    @Test
    public void getPerson_Exist() throws Exception {
        MockHttpServletRequestBuilder requestBuilders = MockMvcRequestBuilders.get("/crud/getPerson/?personID=203");

        MvcResult mvcResult = mockMvc.perform(requestBuilders)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        String outputResponce = mvcResult.getResponse().getContentAsString();

        Person outputPerson = objectMapper.readValue(outputResponce, Person.class);

        LOGGER.info("Person Object Returns from the API is {}", outputPerson);

        assertEquals("Ranbir", outputPerson.getFirstName());

    }


    /**
     * This method can read a file from the given path and return the contents as a string
     *
     * @param filepath
     * @return Contains a file as a String
     * @throws IOException
     */
    static String getJson(String filepath) throws IOException {
        Resource resource = new ClassPathResource(filepath);
        return Files.readString(resource.getFile().toPath());
    }

    @Nested
    class UpdateDeletePersonTest {
        @BeforeEach
        public void insertPerson_Exists() throws Exception {

            MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/crud/insertPerson/")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(getJson("test-data/create-person.json"));

            LOGGER.info("Json of the person to be inserted  is {}:", getJson("test-data/create-person.json"));

            MvcResult mvcResult = mockMvc.perform(requestBuilder)
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andReturn();

            String responceString = mvcResult.getResponse().getContentAsString();
            Person insertedPerson = objectMapper.readValue(responceString, Person.class);

            LOGGER.info("Person Object after being inserted by the api call is this {}", insertedPerson);
            assertTrue(insertedPerson.getPersonID() != 0);
            assertEquals("chowdary".toUpperCase(), insertedPerson.getLastName().toUpperCase());


            personThreadLocal.set(insertedPerson);
        }

        @Test
        public void updatePerson() throws Exception {
            Person person = personThreadLocal.get();
            LOGGER.info("Person object before the update is {}",person);

            person.setFirstName("Kamatham");
            MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.put("/crud/updatePerson?personID="+person.getPersonID())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(person));

            MvcResult mvcResult = mockMvc.perform(requestBuilder)
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andReturn();

            String outputResponeString = mvcResult.getResponse().getContentAsString();
            Person updatedPerson = objectMapper.readValue(outputResponeString, Person.class);

            LOGGER.info("Updated Person aafter the API call is {}",updatedPerson);

            assertEquals(person.getPersonID(),updatedPerson.getPersonID());

            assertEquals("kamatham".toUpperCase(),person.getFirstName().toUpperCase());
        }

        @Test
        public void deletePerson() throws Exception {
            Person person = personThreadLocal.get();
            MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/crud/deletePerson/" + person.getPersonID());

            ResultActions outputResult = mockMvc.perform(requestBuilder)
                    .andExpect(MockMvcResultMatchers.status().isOk());
        }

    }
}