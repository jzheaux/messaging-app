/*
 * Copyright 2002-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package sample.data;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

/**
 * @author Joe Grandja
 */
public interface MessageRepository extends CrudRepository<Message, Long> {

	@Query("select m from Message m where m.toId = ?#{principal.claims['user_name']}")
	Collection<Message> getInbox();

	@Query("select m from Message m where m.fromId = ?#{principal.claims['user_name']}")
	Collection<Message> getSent();
}