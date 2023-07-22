package com.example.demo.token;

import java.util.Optional;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TokenRepository extends JpaRepository<Token, Integer> {
	@Query(value = "select t from Token t inner join Business b on t.user.id = b.id where b.id = :id and (t.expired = false or t.revoked = false)")
	   List<Token> findAllValidTokenByBusiness(int id);
		  Optional<Token> findByToken(String token);
}
