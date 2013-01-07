/*
 * Copyright 2012 Alin Vasile
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
package com.github.alinvasile.jsla.core;

/**
 * 
 * Represents a user accessing a certain resource.
 * 
 * @author Alin Vasile @ since 1.0
 */
public interface Authority {

    /**
     * @return the username used when accessing a resource.
     */
    public String getUsername();

    /**
     * @return the group name this authority belongs to.
     */
    public String getGroup();

    /**
     * @return true if the user is anonymous (i.e. no username), false otherwise.
     */
    boolean isAnonymous();

    /**
     * @return the role names this authority has.
     */
    String[] getRoles();

}
