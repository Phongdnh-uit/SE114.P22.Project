package com.se114p12.backend.neo4j.repositories;

import com.se114p12.backend.neo4j.entities.UserNode;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserNeo4jRepository extends Neo4jRepository<UserNode, Long> {}
