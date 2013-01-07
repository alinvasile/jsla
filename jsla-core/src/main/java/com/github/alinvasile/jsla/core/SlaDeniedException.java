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
 * Exception thrown when the SLA engine denies the request of the authority
 * accessing a sla-protected resource.
 * 
 * @author Alin Vasile
 * @since 1.0
 * 
 */
public class SlaDeniedException extends RuntimeException {

    private static final long serialVersionUID = -2760086542600346586L;

    public SlaDeniedException() {
        super();
    }

    public SlaDeniedException(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }

    public SlaDeniedException(String arg0) {
        super(arg0);
    }

    public SlaDeniedException(Throwable arg0) {
        super(arg0);
    }

}
