
package tn.dksoft.authentification.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;

import tn.dksoft.authentification.entity.AppUser;

@Repository
@CrossOrigin("http://localhost:4200")
public interface AppUserRepository extends JpaRepository<AppUser, Long> {

	AppUser findByUserName(String userName);

	List<AppUser> findByUserNameContaining(@Param("search") String search);
}
