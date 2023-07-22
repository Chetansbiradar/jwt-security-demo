package com.example.demo.token;

import java.util.Optional;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TokenRepository extends JpaRepository<Token, Integer> {

	@Query(value = "select t from Token t inner join Business u on t.user.id = u.id where u.id = :id and (t.expired = false or t.revoked = false) ")
   List<Token> findAllValidTokenByBusiness(String username);

		  Optional<Token> findByToken(String token);
}
