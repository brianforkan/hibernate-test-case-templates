package org.hibernate.bugs;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Persistence;

import org.hibernate.annotations.DiscriminatorOptions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.Serializable;
import java.util.Objects;

/**
 * This template demonstrates how to develop a test case for Hibernate ORM, using the Java Persistence API.
 */
public class JPAUnitTestCase {

	private EntityManagerFactory entityManagerFactory;

	@Before
	public void init() {
		entityManagerFactory = Persistence.createEntityManagerFactory( "templatePU" );
	}

	@After
	public void destroy() {
		entityManagerFactory.close();
	}

	// Entities are auto-discovered, so just add them anywhere on class-path
	// Add your tests, using standard JUnit.
	@Test
	public void hhh123Test() throws Exception {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		entityManager.getTransaction().begin();
		NodeA m1 = new NodeA(new NodeId("1", "NODEA"), "node a");
		entityManager.persist(m1);
		NodeB e1 = new NodeB(new NodeId("1", "NODEB"), "node b");
		entityManager.persist(e1);
		entityManager.getTransaction().commit();
		entityManager.close();
	}

	@Entity(name = "Node")
	@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
	@DiscriminatorColumn(name = "TYPE")
	@DiscriminatorOptions(insert = false)
	public static class Node {
		@EmbeddedId
		private NodeId id;

		@Column(name = "NAME")
		private String name;

		public Node(NodeId id, String name) {
			this.id = id;
			this.name = name;
		}

		public NodeId getId() {
			return id;
		}

		public void setId(NodeId id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		protected Node() {

		}
	}

	@Entity
	@DiscriminatorValue("NODEA")
	public static class NodeA extends Node {
		protected NodeA() {

		}

		public NodeA(NodeId nodeId, String name) {
			super(nodeId, name);
		}
	}

	@Entity
	@DiscriminatorValue("NODEB")
	public static class NodeB extends Node {
		protected NodeB() {

		}

		public NodeB(NodeId nodeId, String name) {
			super(nodeId, name);
		}
	}

	@Embeddable
	public static class NodeId implements Serializable {
		@Column(name = "ID")
		private String id;

		public String getId() {
			return id;
		}
		public NodeId() {}

		public NodeId(String id, String type) {
			this.id = id;
			this.type = type;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) {
				return true;
			}
			if (o == null || getClass() != o.getClass()) {
				return false;
			}

			NodeId nodeId = (NodeId) o;

			if (!Objects.equals(id, nodeId.id)) {
				return false;
			}
			return Objects.equals(type, nodeId.type);
		}

		@Override
		public int hashCode() {
			int result = id != null ? id.hashCode() : 0;
			result = 31 * result + (type != null ? type.hashCode() : 0);
			return result;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}
		@Column(name = "TYPE")
		private String type;
	}
}
