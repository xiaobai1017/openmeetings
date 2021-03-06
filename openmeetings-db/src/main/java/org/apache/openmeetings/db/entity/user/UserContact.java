/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License") +  you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.openmeetings.db.entity.user;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.apache.openjpa.persistence.jdbc.ForeignKey;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Entity
@NamedQueries({
@NamedQuery(name = "deleteUserContact", query = "delete from UserContact u where u.id = :id")
, @NamedQuery(name = "deleteAllUserContacts", query = "delete from UserContact u where u.owner.id = :ownerId")
, @NamedQuery(name = "getContactByUserOwner", query = "SELECT c FROM UserContact c WHERE c.contact.id = :userId AND c.owner.id = :ownerId AND c.contact.deleted = false")
, @NamedQuery(name = "getContactsByUserAndStatus", query = "select c from UserContact c " +
		"where c.owner.id = :ownerId " +
		"AND c.pending = :pending " +
		"AND c.contact.deleted = false")
, @NamedQuery(name = "getContactRequestsByUserAndStatus", query = "select c from UserContact c " +
		"where c.contact.id = :userId " +
		"AND c.pending = :pending " +
		"AND c.contact.deleted = false")
, @NamedQuery(name = "getContactsByUser", query = "SELECT c FROM UserContact c " +
		"WHERE c.contact.id = :userId " +
		"AND c.contact.deleted = false ORDER BY c.pending DESC")
, @NamedQuery(name = "countContactsByUser", query = "select COUNT(c) from UserContact c " +
		"where c.contact.id = :userId " +
		"AND c.contact.deleted = false")
, @NamedQuery(name = "getUserContactsById", query = "SELECT c FROM UserContact c WHERE c.id = :id")
, @NamedQuery(name = "getUserContacts", query = "SELECT c FROM UserContact c ORDER BY c.id")
})
@Table(name = "user_contact")
@Root(name="usercontact")
public class UserContact implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
	@Element(data = true, name = "userContactId")
	private Long id;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="user_id")
	@Element(data=true, required=false)
	private User contact;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="owner_id")
	@ForeignKey(enabled = true)
	@Element(data=true, required=false)
	private User owner;

	@Column(name="pending", nullable = false)
	@Element(data=true)
	private boolean pending;

	@Column(name="inserted")
	private Date inserted;

	@Column(name="updated")
	private Date updated;

	@Column(name="share_calendar", nullable = false)
	@Element(data=true, required=false)
	private boolean shareCalendar;

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	public User getContact() {
		return contact;
	}
	public void setContact(User contact) {
		this.contact = contact;
	}

	public User getOwner() {
		return owner;
	}
	public void setOwner(User owner) {
		this.owner = owner;
	}

	public boolean isPending() {
		return pending;
	}
	public void setPending(boolean pending) {
		this.pending = pending;
	}

	public Date getInserted() {
		return inserted;
	}
	public void setInserted(Date inserted) {
		this.inserted = inserted;
	}

	public Date getUpdated() {
		return updated;
	}
	public void setUpdated(Date updated) {
		this.updated = updated;
	}

	public boolean getShareCalendar() {
		return shareCalendar;
	}
	public void setShareCalendar(boolean shareCalendar) {
		this.shareCalendar = shareCalendar;
	}
}
