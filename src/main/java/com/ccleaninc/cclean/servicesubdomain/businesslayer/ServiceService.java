package com.ccleaninc.cclean.servicesubdomain.businesslayer;

import com.ccleaninc.cclean.servicesubdomain.presentationlayer.ServiceResponseModel;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public interface ServiceService {

    List<ServiceResponseModel> getAllServices();
}
