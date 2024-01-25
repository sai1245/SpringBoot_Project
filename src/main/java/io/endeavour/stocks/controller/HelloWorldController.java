package io.endeavour.stocks.controller;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.time.LocalDate;
import java.util.Collections;

@RestController
public class HelloWorldController {

    @GetMapping(value = "/helloworld")
    public String firstMethod(){

        return "Hello World";
    }

    @GetMapping(value = "hello/world")
    public String callingHelloWorld(){
        return "Hello world Again";
    }

    /**
     * Example of single path parameter
     */
    @GetMapping(value = "/concatString/{inputString}")         //Annotation............
    public String concatString(@PathVariable(value = "inputString") String sampleString){
        return sampleString+sampleString;
    }

    /**
     * Example of Two path parameter
     * @param sampleDate for the date porameter we have to mention the format using @DateTimeFormat annotation.
     */
    @GetMapping(value = "/concatStringDate/{inputString}/{inputDate}")
    public String concatStringDate(@PathVariable(value = "inputString") String sampleString,
                                  @PathVariable(value = "inputDate")
                                  @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate sampleDate){
        return sampleString+sampleDate.plusYears(10);
    }

    @GetMapping(value = "/concatStringDateQP/queryParam/{inputString}")
    public String concatStringDateQP(@PathVariable(value = "inputString") String sampleString,
                                     @RequestParam(value = "inputDate")
                                     @DateTimeFormat(pattern = "yyyy-MM-dd")LocalDate sampleDate){
        return sampleString+sampleDate;
    }


    //Example
    @GetMapping(value = "/concatStringDateQPExample/queryParam/{inputString}")
    public String concatStringDateQPExample(@PathVariable(value = "inputString") String sampleString,
                                     @RequestParam(value = "initial") String initial,
                                     @RequestParam(value = "inputDate")
                                     @DateTimeFormat(pattern = "yyyy-MM-dd")LocalDate sampleDate){
        return initial+sampleString+sampleDate;
    }

    @PostMapping(value = "sortTickers/{inputString}")
    public List<String> sortTickerList(@PathVariable(value = "inputString") String sampleString,
                                       @RequestParam(value = "inputDate")
                                       @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate sampleDate,
                                       @RequestBody List<String> tickerList){
        Collections.sort(tickerList);
        return tickerList;

    }
}
