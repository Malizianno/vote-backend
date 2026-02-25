package ro.cristiansterie.vote.service;

import java.lang.invoke.MethodHandles;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

public class GenericService {
    protected static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    protected ModelMapper mapper = new ModelMapper();
    protected ObjectMapper objectMapper = new ObjectMapper();
}
