package org.upgrad.upstac.testrequests.consultation;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.upgrad.upstac.config.security.UserLoggedInService;
import org.upgrad.upstac.exception.AppException;
import org.upgrad.upstac.testrequests.RequestStatus;
import org.upgrad.upstac.testrequests.TestRequest;
import org.upgrad.upstac.testrequests.TestRequestQueryService;
import org.upgrad.upstac.testrequests.TestRequestUpdateService;
import org.upgrad.upstac.testrequests.flow.TestRequestFlowService;
import org.upgrad.upstac.users.User;

import javax.validation.ConstraintViolationException;
import java.util.List;

import static org.upgrad.upstac.exception.UpgradResponseStatusException.asBadRequest;
import static org.upgrad.upstac.exception.UpgradResponseStatusException.asConstraintViolation;


@RestController
@RequestMapping("/api/consultations")
public class ConsultationController {

    Logger log = LoggerFactory.getLogger(ConsultationController.class);




    @Autowired
    private TestRequestUpdateService testRequestUpdateService;

    @Autowired
    private TestRequestQueryService testRequestQueryService;


    @Autowired
    TestRequestFlowService  testRequestFlowService;

    @Autowired
    private UserLoggedInService userLoggedInService;



    @GetMapping("/in-queue")
    @PreAuthorize("hasAnyRole('DOCTOR')")
    public List<TestRequest> getForConsultations()  {

        // Implementation for getting consultation based on request status ( QUICK FIND SHORTCUT - #445114)

        return testRequestQueryService.findBy(RequestStatus.LAB_TEST_COMPLETED);

        //TODO: To be removed later if the implementation is not required in later builds
       // throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED,"Not implemented");


    }

    @GetMapping
    @PreAuthorize("hasAnyRole('DOCTOR')")
    public List<TestRequest> getForDoctor()  {

        // Implementation for listing consultation requests ( QUICK FIND SHORTCUT - #445113)
        User doctor = userLoggedInService.getLoggedInUser();
        return testRequestQueryService.findByDoctor(doctor);

        //TODO: to be removed later if not needed
        //throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED,"Not implemented");




    }



    @PreAuthorize("hasAnyRole('DOCTOR')")
    @PutMapping("/assign/{id}")
    public TestRequest assignForConsultation(@PathVariable Long id) {

        // Implementation for updating consultation based results ( QUICK FIND SHORTCUT - #445112)
        try {

            User doctor = userLoggedInService.getLoggedInUser();
            return testRequestUpdateService.assignForConsultation(id, doctor);

            // TODO: required cleanup if this is not needed in later builds
           // throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED,"Not implemented");

        }catch (AppException e) {
            throw asBadRequest(e.getMessage());
        }
    }



    @PreAuthorize("hasAnyRole('DOCTOR')")
    @PutMapping("/update/{id}")
    public TestRequest updateConsultation(@PathVariable Long id,@RequestBody CreateConsultationRequest testResult) {

        // Implementation for updating consultation based on test results ( QUICK FIND SHORTCUT - #445111)
        try {

            User doctor = userLoggedInService.getLoggedInUser();
            return testRequestUpdateService.updateConsultation(id,testResult, doctor );

            // TODO: required cleanup if this is not needed in later builds
            //throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED,"Not implemented");


        } catch (ConstraintViolationException e) {
            throw asConstraintViolation(e);
        }catch (AppException e) {
            throw asBadRequest(e.getMessage());
        }
    }



}
