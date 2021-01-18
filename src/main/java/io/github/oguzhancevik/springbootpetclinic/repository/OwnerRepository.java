package io.github.oguzhancevik.springbootpetclinic.repository;

import io.github.oguzhancevik.springbootpetclinic.model.Owner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OwnerRepository extends JpaRepository<Owner, Long> {

    List<Owner> findByLastName(@Param("ln") String lastName);
}
