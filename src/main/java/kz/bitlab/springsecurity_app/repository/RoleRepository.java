package kz.bitlab.springsecurity_app.repository;

import kz.bitlab.springsecurity_app.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role,Long> {

}
