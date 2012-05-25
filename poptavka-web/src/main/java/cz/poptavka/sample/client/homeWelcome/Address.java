package cz.poptavka.sample.client.homeWelcome;

/*
 GWT Validation Framework - A JSR-303 validation framework for GWT

 (c) 2008 gwt-validation contributors (http://code.google.com/p/gwt-validation/) 

 Licensed to the Apache Software Foundation (ASF) under one
 or more contributor license agreements.  See the NOTICE file
 distributed with this work for additional information
 regarding copyright ownership.  The ASF licenses this file
 to you under the Apache License, Version 2.0 (the
 "License"); you may not use this file except in compliance
 with the License.  You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing,
 software distributed under the License is distributed on an
 "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 KIND, either express or implied.  See the License for the
 specific language governing permissions and limitations
 under the License.
*/

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.validation.groups.Default;

public class Address {

	@NotNull(message="{address.notNull.zip}")
	@Size(min=5, max=10, message="{address.size.zip}")
	private String zip = null;

        @NotNull(message="{address.notNull.street}")
	@Size(min=3, message="{address.size.street}", groups=Default.class)
	private String street = null;

	@NotNull(message="{address.notNull.state}")
	@Size(min=4, message="{address.size.state}")
	private String state = null;

	@NotNull(message="{address.notNull.city}")
	@Size(min=4, message="{address.size.city}")
	private String city = null;

//	@NotNull
//	@Valid
//	private Person owner = null;
	
	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

//	public Person getOwner() {
//		return owner;
//	}
//
//	public void setOwner(Person owner) {
//		this.owner = owner;
//	}
}
