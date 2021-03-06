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
package org.apache.openmeetings.db.entity.room;

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
import org.apache.openmeetings.db.entity.HistoricalEntity;
import org.apache.openmeetings.db.entity.user.User;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Entity
@NamedQueries({
@NamedQuery(name = "getRoomModeratorById", query = "select c from RoomModerator as c where c.id = :id")
, @NamedQuery(name = "getRoomModeratorsByIds", query = "select c from RoomModerator as c where c.id IN :ids")
, @NamedQuery(name = "getRoomModeratorByRoomId", query = "select c from RoomModerator as c where c.roomId = :roomId AND c.deleted = false")
, @NamedQuery(name = "getRoomModeratorByUserAndRoomId", query = "select c from RoomModerator as c "
		+ "where c.roomId = :roomId AND c.deleted = false AND c.user.id = :userId")
})
@Table(name = "room_moderator")
@Root(name = "room_moderator")
public class RoomModerator extends HistoricalEntity {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "roomId")
	private Long roomId;

	@Column(name = "is_supermoderator", nullable = false)
	@Element(name = "is_supermoderator", data = true)
	private boolean superModerator;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "user_id")
	@ForeignKey(enabled = true)
	@Element(name = "user_id", data = true, required = false)
	private User user;

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	public boolean isSuperModerator() {
		return superModerator;
	}

	public void setSuperModerator(boolean superModerator) {
		this.superModerator = superModerator;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Long getRoomId() {
		return roomId;
	}

	public void setRoomId(Long roomId) {
		this.roomId = roomId;
	}
}
