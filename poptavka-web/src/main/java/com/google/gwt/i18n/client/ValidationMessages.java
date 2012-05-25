/*
 * Copyright 2010 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.google.gwt.i18n.client;

/**
 * Custom validation messages. Interface to represent the constants contained in
 * resource bundle:
 * 'com/google/gwt/sample/validation/client/ValidationMessages.properties'
 * TODO(nchalko) move this to the root package so client and server can share
 * the same properties files.
 */
public interface ValidationMessages extends ConstantsWithLookup {

    /**
     * Translated "Name must be at least {size} characters long.".
     *
     * @return translated "Name must be at least {min} characters long."
     */
//    @DefaultStringValue("Name must be at least {min} characters long.")
//    @Key("custom.name.size.message")
//    String custom_name_size_message();

    @Key("address.notNull.state")
    String address_notNull_state();

    @Key("address.notNull.city")
    String address_notNull_city();

    @Key("address.notNull.street")
    String address_notNull_street();
    
    @Key("address.notNull.zip")
    String address_notNull_zip();


    @Key("address.size.state")
    String address_size_state();

    @Key("address.size.city")
    String address_size_city();

    @Key("address.size.street")
    String address_size_street();

    @Key("address.size.zip")
    String address_size_zip();
}
