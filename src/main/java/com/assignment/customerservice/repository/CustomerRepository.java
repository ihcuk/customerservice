package com.assignment.customerservice.repository;

import com.assignment.customerservice.domain.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {
    public Customer findByPincode(Integer pincode);
    public Customer findByName(String name);
    @Query("SELECT c.userid FROM Customer c WHERE c.name = :name")
    public Integer findUseridByName(String name);
    @Query("SELECT c.profilephoto FROM Customer c WHERE c.name = :name")
    public byte[] findProfilephotoByName(String name);
}
