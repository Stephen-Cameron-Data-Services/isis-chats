/*
 *  Copyright 2014 Dan Haywood
 *
 *  Licensed under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */
package au.com.scds.chats.dom.bulk;

public enum State {
    OPEN,
    IN_PROGRESS,
    CLOSED;

    public State next() {
        switch(this) {
            case OPEN:
                return IN_PROGRESS;
            case IN_PROGRESS:
                return CLOSED;
            case CLOSED:
                return CLOSED;
            default:
                // can't happen, all states enumerated above
                throw new IllegalStateException();
        }
    }

    public State previous() {
        switch(this) {
            case OPEN:
                return OPEN;
            case IN_PROGRESS:
                return OPEN;
            case CLOSED:
                return IN_PROGRESS;
            default:
                // can't happen, all states enumerated above
                throw new IllegalStateException();
        }
    }

}
