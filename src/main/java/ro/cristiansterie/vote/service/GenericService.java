package ro.cristiansterie.vote.service;

import org.modelmapper.ModelMapper;

import com.fasterxml.jackson.databind.ObjectMapper;

public class GenericService {
    
    protected ModelMapper mapper = new ModelMapper();
    protected ObjectMapper objectMapper = new ObjectMapper();
}
