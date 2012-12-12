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
package com.github.alinvasile.jsla.web.context;

import com.github.alinvasile.jsla.core.AccessMonitor;
import com.github.alinvasile.jsla.core.Authority;
import com.github.alinvasile.jsla.core.SlaDeniedException;

public interface AccessHierarchy {

    public abstract void add(String contextPath, AccessMonitor monitor);

    public abstract void grant(String requestURI, Authority authority) throws SlaDeniedException;

}