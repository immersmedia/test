/**
 * Copyright Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package co.tau.manthan.baseclasses;

import java.util.Date;

public class BArg {
    public static final String ARGTEXT_DEFAULT = "NOT UPDATED YET";

    private String uid;
    private String debateruid;
    private String debaterpic;
    private String debatername;
    private String debatersid;
    private String debateuid;

    private String tilt;
    private String msg_type;

    private String argtext;
    private Date ts;


    private String attachimg;

    public BArg() {
    }


    public BArg(String uid, String debateruid, String debaterpic,
                String debatername, String debatersid, String debateuid,
                String tilt, String argtext, String attachimg, String msg_type, Date ts) {
        this.uid = uid;
        this.debateruid = debateruid;
        this.debaterpic = debaterpic;
        this.debatername = debatername;
        this.debatersid = debatersid;
        this.debateuid = debateuid;
        this.tilt = tilt;
        this.argtext = argtext;
        this.attachimg = attachimg;
        this.msg_type = msg_type;
        this.ts = ts;
    }

    public String getMsg_type() {
        return msg_type;
    }

    public void setMsg_type(String msg_type) {
        this.msg_type = msg_type;
    }

    public Date getTs() {
        return ts;
    }

    public void setTs(Date ts) {
        this.ts = ts;
    }

    public String getDebateruid() {
        return debateruid;
    }

    public void setDebateruid(String debateruid) {
        this.debateruid = debateruid;
    }

    public String getDebatersid() {
        return debatersid;
    }

    public void setDebatersid(String debatersid) {
        this.debatersid = debatersid;
    }

    public String getDebateuid() {
        return debateuid;
    }

    public void setDebateuid(String debateuid) {
        this.debateuid = debateuid;
    }

    public String getTilt() {
        return tilt;
    }

    public void setTilt(String tilt) {
        this.tilt = tilt;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setArgtext(String argtext) {
        this.argtext = argtext;
    }

    public String getDebatername() {
        return debatername;
    }

    public void setDebatername(String debatername) {
        this.debatername = debatername;
    }

    public String getDebaterpic() {
        return debaterpic;
    }

    public String getArgtext() {
        return argtext;
    }

    public void setDebaterpic(String debaterpic) {
        this.debaterpic = debaterpic;
    }

    public String getAttachimg() {
        return attachimg;
    }

    public void setAttachimg(String attachimg) {
        this.attachimg = attachimg;
    }
}
