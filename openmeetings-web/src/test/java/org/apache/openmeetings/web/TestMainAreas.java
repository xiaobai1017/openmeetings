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
package org.apache.openmeetings.web;

import static org.apache.openmeetings.util.OpenmeetingsVariables.getWebAppRootKey;
import static org.apache.openmeetings.web.util.OmUrlFragment.TYPE_BACKUP;
import static org.apache.openmeetings.web.util.OmUrlFragment.TYPE_CALENDAR;
import static org.apache.openmeetings.web.util.OmUrlFragment.TYPE_CONFIG;
import static org.apache.openmeetings.web.util.OmUrlFragment.TYPE_CONNECTION;
import static org.apache.openmeetings.web.util.OmUrlFragment.TYPE_EDIT;
import static org.apache.openmeetings.web.util.OmUrlFragment.TYPE_EMAIL;
import static org.apache.openmeetings.web.util.OmUrlFragment.TYPE_GROUP;
import static org.apache.openmeetings.web.util.OmUrlFragment.TYPE_LANG;
import static org.apache.openmeetings.web.util.OmUrlFragment.TYPE_LDAP;
import static org.apache.openmeetings.web.util.OmUrlFragment.TYPE_MESSAGES;
import static org.apache.openmeetings.web.util.OmUrlFragment.TYPE_MY;
import static org.apache.openmeetings.web.util.OmUrlFragment.TYPE_OAUTH2;
import static org.apache.openmeetings.web.util.OmUrlFragment.TYPE_PUBLIC;
import static org.apache.openmeetings.web.util.OmUrlFragment.TYPE_RECORDINGS;
import static org.apache.openmeetings.web.util.OmUrlFragment.TYPE_ROOM;
import static org.apache.openmeetings.web.util.OmUrlFragment.TYPE_USER;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.red5.logging.Red5LoggerFactory.getLogger;

import java.util.function.Consumer;

import org.apache.openmeetings.AbstractWicketTester;
import org.apache.openmeetings.util.OmException;
import org.apache.openmeetings.web.admin.backup.BackupPanel;
import org.apache.openmeetings.web.admin.configurations.ConfigsPanel;
import org.apache.openmeetings.web.admin.connection.ConnectionsPanel;
import org.apache.openmeetings.web.admin.email.EmailPanel;
import org.apache.openmeetings.web.admin.groups.GroupsPanel;
import org.apache.openmeetings.web.admin.labels.LangPanel;
import org.apache.openmeetings.web.admin.ldaps.LdapsPanel;
import org.apache.openmeetings.web.admin.oauth.OAuthPanel;
import org.apache.openmeetings.web.admin.rooms.RoomsPanel;
import org.apache.openmeetings.web.admin.users.UsersPanel;
import org.apache.openmeetings.web.common.BasePanel;
import org.apache.openmeetings.web.pages.MainPage;
import org.apache.openmeetings.web.pages.NotInitedPage;
import org.apache.openmeetings.web.pages.auth.SignInPage;
import org.apache.openmeetings.web.pages.install.InstallWizardPage;
import org.apache.openmeetings.web.room.RoomPanel;
import org.apache.openmeetings.web.room.wb.AbstractWbPanel;
import org.apache.openmeetings.web.user.calendar.CalendarPanel;
import org.apache.openmeetings.web.user.dashboard.OmDashboardPanel;
import org.apache.openmeetings.web.user.profile.SettingsPanel;
import org.apache.openmeetings.web.user.record.RecordingsPanel;
import org.apache.openmeetings.web.user.rooms.RoomsSelectorPanel;
import org.apache.openmeetings.web.util.OmUrlFragment.AreaKeys;
import org.apache.wicket.authorization.UnauthorizedInstantiationException;
import org.apache.wicket.behavior.AbstractAjaxBehavior;
import org.junit.Test;
import org.slf4j.Logger;

import com.googlecode.wicket.jquery.ui.widget.tabs.TabbedPanel;

public class TestMainAreas extends AbstractWicketTester {
	private static final Logger log = getLogger(TestMainAreas.class, getWebAppRootKey());

	@Test
	public void testDashboard() throws OmException {
		testArea(regularUsername, p -> {
			tester.assertComponent(PATH_CHILD, OmDashboardPanel.class);
		});
	}

	private void checkArea(AreaKeys area, String type, Class<? extends BasePanel> clazz, String... users) throws OmException {
		checkArea(area, type, clazz, null, users);
	}

	private void checkArea(AreaKeys area, String type, Class<? extends BasePanel> clazz, Consumer<MainPage> consumer, String... users) throws OmException {
		for (String user : users) {
			log.debug("Positive test:: area: {}, type: {} for user: {}", area, type, user);
			testArea(user, p -> {
				tester.getRequest().setParameter(area.name(), type);
				tester.executeBehavior((AbstractAjaxBehavior)p.getBehaviorById(1));
				tester.assertComponent(PATH_CHILD, clazz);
				if (consumer != null) {
					consumer.accept(p);
				}
			});
		}
	}

	private void checkUnauthArea(AreaKeys area, String type, String... users) throws OmException {
		for (String user : users) {
			log.debug("Positive test:: area: {}, type: {} for user: {}", area, type, user);
			testArea(user, p -> {
				tester.getRequest().setParameter(area.name(), type);
				try {
					tester.executeBehavior((AbstractAjaxBehavior)p.getBehaviorById(1));
					fail("Not authorized");
				} catch (UnauthorizedInstantiationException e) {
					assertTrue("Exception is expected", true);
				}
			});
		}
	}

	@Test
	public void testCalendar() throws OmException {
		checkArea(AreaKeys.user, TYPE_CALENDAR, CalendarPanel.class, regularUsername);
	}

	@Test
	public void testRecordings() throws OmException {
		checkArea(AreaKeys.user, TYPE_RECORDINGS, RecordingsPanel.class, regularUsername);
	}

	@Test
	public void testRoomsPublic() throws OmException {
		checkArea(AreaKeys.rooms, TYPE_PUBLIC, RoomsSelectorPanel.class, regularUsername);
	}

	@Test
	public void testRoomsGroup() throws OmException {
		checkArea(AreaKeys.rooms, TYPE_GROUP, RoomsSelectorPanel.class, regularUsername);
	}

	@Test
	public void testRoomsMy() throws OmException {
		checkArea(AreaKeys.rooms, TYPE_MY, RoomsSelectorPanel.class, regularUsername);
	}

	@Test
	public void testRoomsProfileMessages() throws OmException {
		checkArea(AreaKeys.profile, TYPE_MESSAGES, SettingsPanel.class, p -> {
			TabbedPanel tp = (TabbedPanel)p.get("main-container:main:contents:child:tabs");
			tester.executeBehavior((AbstractAjaxBehavior)tp.getBehaviorById(0)); //create behavior
			for (int i = 0; i <= tp.getLastTabIndex(); ++i) {
				tester.getRequest().setParameter("index", String.valueOf(i));
				tester.executeBehavior((AbstractAjaxBehavior)tp.getBehaviorById(1)); // activate
				//add visibility check
			}
		}, regularUsername);
	}

	@Test
	public void testRoomsProfileEdit() throws OmException {
		checkArea(AreaKeys.profile, TYPE_EDIT, SettingsPanel.class, regularUsername);
	}

	@Test
	public void testNonExistent() throws OmException {
		checkArea(AreaKeys.profile, "bla", OmDashboardPanel.class, regularUsername);
	}

	@Test
	public void testAdminUsers() throws OmException {
		checkArea(AreaKeys.admin, TYPE_USER, UsersPanel.class, adminUsername, groupAdminUsername);
	}

	@Test
	public void testAdminUsers1() throws OmException {
		checkUnauthArea(AreaKeys.admin, TYPE_USER, regularUsername);
	}

	@Test
	public void testAdminConnections() throws OmException {
		checkArea(AreaKeys.admin, TYPE_CONNECTION, ConnectionsPanel.class, adminUsername);
	}

	@Test
	public void testAdminConnections1() throws OmException {
		checkUnauthArea(AreaKeys.admin, TYPE_CONNECTION, groupAdminUsername, regularUsername);
	}

	@Test
	public void testAdminGroups() throws OmException {
		checkArea(AreaKeys.admin, TYPE_GROUP, GroupsPanel.class, adminUsername, groupAdminUsername);
	}

	@Test
	public void testAdminGroups1() throws OmException {
		checkUnauthArea(AreaKeys.admin, TYPE_GROUP, regularUsername);
	}

	@Test
	public void testAdminRooms() throws OmException {
		checkArea(AreaKeys.admin, TYPE_ROOM, RoomsPanel.class, adminUsername, groupAdminUsername);
	}

	@Test
	public void testAdminRooms1() throws OmException {
		checkUnauthArea(AreaKeys.admin, TYPE_ROOM, regularUsername);
	}

	@Test
	public void testAdminConfigs() throws OmException {
		checkArea(AreaKeys.admin, TYPE_CONFIG, ConfigsPanel.class, adminUsername);
	}

	@Test
	public void testAdminConfigs1() throws OmException {
		checkUnauthArea(AreaKeys.admin, TYPE_CONFIG, groupAdminUsername, regularUsername);
	}

	@Test
	public void testAdminLang() throws OmException {
		checkArea(AreaKeys.admin, TYPE_LANG, LangPanel.class, adminUsername);
	}

	@Test
	public void testAdminLang1() throws OmException {
		checkUnauthArea(AreaKeys.admin, TYPE_LANG, groupAdminUsername, regularUsername);
	}

	@Test
	public void testAdminLdap() throws OmException {
		checkArea(AreaKeys.admin, TYPE_LDAP, LdapsPanel.class, adminUsername);
	}

	@Test
	public void testAdminLdap1() throws OmException {
		checkUnauthArea(AreaKeys.admin, TYPE_LDAP, groupAdminUsername, regularUsername);
	}

	@Test
	public void testAdminOauth() throws OmException {
		checkArea(AreaKeys.admin, TYPE_OAUTH2, OAuthPanel.class, adminUsername);
	}

	@Test
	public void testAdminOauth1() throws OmException {
		checkUnauthArea(AreaKeys.admin, TYPE_OAUTH2, groupAdminUsername, regularUsername);
	}

	@Test
	public void testAdminBackup() throws OmException {
		checkArea(AreaKeys.admin, TYPE_BACKUP, BackupPanel.class, adminUsername);
	}

	@Test
	public void testAdminBackup1() throws OmException {
		checkUnauthArea(AreaKeys.admin, TYPE_BACKUP, groupAdminUsername, regularUsername);
	}

	@Test
	public void testAdminEmail() throws OmException {
		checkArea(AreaKeys.admin, TYPE_EMAIL, EmailPanel.class, adminUsername);
	}

	@Test
	public void testAdminEmail1() throws OmException {
		checkUnauthArea(AreaKeys.admin, TYPE_EMAIL, groupAdminUsername, regularUsername);
	}

	@Test
	public void testAdminBad() throws OmException {
		checkArea(AreaKeys.admin, "BAD", OmDashboardPanel.class, adminUsername);
	}

	private void testRoom(Long id) throws OmException {
		checkArea(AreaKeys.room, String.valueOf(id), RoomPanel.class, p -> {
			RoomPanel rp = (RoomPanel)p.get(PATH_CHILD);
			tester.executeBehavior((AbstractAjaxBehavior)rp.getBehaviorById(0)); //room enter
			AbstractWbPanel wb = (AbstractWbPanel)rp.get("roomContainer:wb-area:whiteboard");
			tester.executeBehavior((AbstractAjaxBehavior)wb.getBehaviorById(0)); //wb load
		}, regularUsername);
	}

	@Test
	public void testRoom5() throws OmException {
		// public presentation room
		testRoom(5L);
	}

	@Test
	public void testRoom1() throws OmException {
		//public interview room
		testRoom(1L);
	}

	@Test
	public void testRoomBad() throws OmException {
		checkArea(AreaKeys.room, "BAD", OmDashboardPanel.class, adminUsername);
	}

	@Test
	public void testInstallNotAccessible() {
		tester.startPage(InstallWizardPage.class);
		tester.assertRenderedPage(SignInPage.class);
	}

	@Test
	public void testUnavailNotAccessible() {
		tester.startPage(NotInitedPage.class);
		tester.assertRenderedPage(SignInPage.class);
	}
}
