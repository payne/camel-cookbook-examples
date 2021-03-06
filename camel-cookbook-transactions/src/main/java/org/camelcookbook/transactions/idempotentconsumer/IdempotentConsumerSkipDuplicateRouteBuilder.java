/*
 * Copyright (C) Scott Cranton and Jakub Korab
 * https://github.com/CamelCookbook
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.camelcookbook.transactions.idempotentconsumer;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.processor.idempotent.MemoryIdempotentRepository;

class IdempotentConsumerSkipDuplicateRouteBuilder extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        from("direct:in")
            .log("Received message ${header[messageId]}")
            .idempotentConsumer(header("messageId"), new MemoryIdempotentRepository()).skipDuplicate(false)
                .choice()
                    .when(property(Exchange.DUPLICATE_MESSAGE))
                        .log("Duplicate")
                        .to("mock:duplicate")
                    .otherwise()
                        .to("mock:ws")
                .endChoice()
            .end()
            .log("Completing")
            .to("mock:out");
    }
}
