package com.ccleaninc.cclean.servicesubdomain.datalayer;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ServiceRepository extends JpaRepository<Service, String> {

Service findServiceByServiceIdentifier_ServiceId(String serviceId);

List<Service> findByTitleContainingIgnoreCase(String title);

}
