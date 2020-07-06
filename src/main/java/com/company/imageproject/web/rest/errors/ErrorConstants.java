package com.company.imageproject.web.rest.errors;

import lombok.experimental.UtilityClass;

import java.net.URI;

@UtilityClass
public class ErrorConstants {

    public final String ERR_CONCURRENCY_FAILURE = "error.concurrencyFailure";
    public final String ERR_VALIDATION = "error.validation";
    public final String PROBLEM_BASE_URL = "https://www.jhipster.tech/problem";
    public final URI DEFAULT_TYPE = URI.create(PROBLEM_BASE_URL + "/problem-with-message");
    public final URI CONSTRAINT_VIOLATION_TYPE = URI.create(PROBLEM_BASE_URL + "/constraint-violation");

}
