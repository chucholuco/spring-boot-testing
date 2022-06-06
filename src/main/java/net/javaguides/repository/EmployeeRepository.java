package net.javaguides.repository;

import net.javaguides.model.Employee;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

     Optional<Employee> findByEmail(String email);

     //Define custom query using JPQL with index parameters
     @Query("select  e  from Employee e where e.firstName = ?1 and e.lastName = ?2")
     List<Employee> findByJPQL(String fistName, String lastName);

     //Define custom query using JPQL with named parameters
     @Query("select  e  from Employee e where e.firstName =:firstName and e.lastName =:lastName")
     List<Employee> findByJPQLNamedParams(@Param("firstName") String fistName, @Param("lastName") String lastName);

     //Define custom query using Native SQL with index params
     @Query(value = "select * from employee e where e.fist_name =?1 and e.last_name =?2", nativeQuery = true)
     List<Employee> findByNativeSQL(String firstName, String lastName);

     //Define custom query using Native SQL with named params
     @Query(value = "select * from employee e where e.fist_name =:firstName and e.last_name =:lastName", nativeQuery = true)
     List<Employee> findByNativeSQLNamedParams(@Param("firstName") String firstName, @Param("lastName") String lastName);


}
