package com.se114p12.backend.neo4j.entities;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Version;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

@Getter
@Setter
@Node("CategoryNode")
public class CategoryNode {

  @Id private Long id;

  @Version private Long version;
}
