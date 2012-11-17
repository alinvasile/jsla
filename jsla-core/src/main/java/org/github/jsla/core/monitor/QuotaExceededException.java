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
package org.github.jsla.core.monitor;

public class QuotaExceededException extends TransactionDeniedException {

	private static final long serialVersionUID = 1916679904275255841L;

	public QuotaExceededException() {
		super();
	}

	public QuotaExceededException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public QuotaExceededException(String arg0) {
		super(arg0);
	}

	public QuotaExceededException(Throwable arg0) {
		super(arg0);
	}

}
