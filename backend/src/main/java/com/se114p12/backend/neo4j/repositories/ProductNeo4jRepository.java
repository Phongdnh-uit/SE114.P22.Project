package com.se114p12.backend.neo4j.repositories;

import com.se114p12.backend.neo4j.entities.OrderedRelationship;
import com.se114p12.backend.neo4j.entities.ProductNode;
import java.util.Collection;
import java.util.List;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductNeo4jRepository extends Neo4jRepository<ProductNode, Long> {

  @Query(
      "MATCH (p:ProductNode)<-[:ORDERED]-(:UserNode) "
          + "WITH p, count(*) AS orderCount "
          + "RETURN p.id "
          + "ORDER BY orderCount DESC "
          + "LIMIT 5")
  List<Long> findPopularProducts();

  @Query(
      "MATCH (u:UserNode {id: $userId})-[:ORDERED]->(p:ProductNode) "
          + "WITH p, count(*) AS orderCount "
          + "RETURN p.id "
          + "ORDER BY orderCount DESC "
          + "LIMIT 5")
  List<Long> findReOrderRecommendation(@Param("userId") Long userId);

  @Query(
      "MATCH (u:UserNode {id:"
          + " $userId})-[:ORDERED]->(p:ProductNode)-[:BELONGS_TO]->(c:CategoryNode) MATCH"
          + " (c)<-[:BELONGS_TO]-(recommended:ProductNode) WHERE NOT (u)-[:ORDERED]->(recommended)"
          + " WITH recommended, count(*) AS score "
          + " RETURN recommended.id ORDER BY score DESC LIMIT 5")
  // + " ORDER BY recommended.rating DESC")
  List<Long> findRecommendByCategory(@Param("userId") Long userId);

  // collaborative filtering by user
  @Query(
      "MATCH (u1:UserNode {id: $userId})-[:ORDERED]->(p:ProductNode)<-[:ORDERED]-(u2:UserNode) "
          + "WHERE u2.id <> $userId "
          + "MATCH (u2)-[o:ORDERED]->(recommended:ProductNode) "
          + "WHERE NOT (u1)-[:ORDERED]->(recommended) "
          + "WITH recommended, count(*) AS score "
          + "RETURN recommended.id ORDER BY score DESC LIMIT 5")
  List<Long> findRecommendedByHistory(@Param("userId") Long userId);

  // co-purchase analysis.
  @Query(
      "MATCH (u1:UserNode {id: $userId})-[:ORDERED]->(p:ProductNode)<-[:ORDERED]-(u2:UserNode) "
          + "WHERE u2.id <> $userId "
          + "MATCH (u2)-[o:ORDERED]->(recommended:ProductNode)<-[:BOUGHT_WITH]-(p) "
          + "WHERE NOT (u1)-[:ORDERED]->(recommended) "
          + "WITH recommended, count(*) AS score "
          + "RETURN recommended.id ORDER BY score DESC LIMIT 5")
  List<Long> findRecommendedByCoPurchase(@Param("userId") Long userId);
}
