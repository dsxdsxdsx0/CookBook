package com.example.dsxdsxdsx0.cookbook.info;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by dsxdsxdsx0 on 2016/8/11.
 */
public class UpdateEntity implements Parcelable {

    /**
     * name : 生活之家
     * version : 1
     * changelog : First release
     * updated_at : 1470226175
     * versionShort : 1.0
     * build : 1
     * installUrl : http://download.fir.im/v2/app/install/57a1dee2ca87a8691e000837?download_token=c0b6471ee5a9ee841e7a40f3ee3d49da
     * install_url : http://download.fir.im/v2/app/install/57a1dee2ca87a8691e000837?download_token=c0b6471ee5a9ee841e7a40f3ee3d49da
     * direct_install_url : http://download.fir.im/v2/app/install/57a1dee2ca87a8691e000837?download_token=c0b6471ee5a9ee841e7a40f3ee3d49da
     * update_url : http://fir.im/cookbook
     * binary : {"fsize":3748008}
     */

    private String name;
    private String version;
    private String changelog;
    private String updated_at;
    private String versionShort;
    private String build;
    private String installUrl;
    private String install_url;
    private String direct_install_url;
    private String update_url;
    private BinaryBean binary;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getChangelog() {
        return changelog;
    }

    public void setChangelog(String changelog) {
        this.changelog = changelog;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getVersionShort() {
        return versionShort;
    }

    public void setVersionShort(String versionShort) {
        this.versionShort = versionShort;
    }

    public String getBuild() {
        return build;
    }

    public void setBuild(String build) {
        this.build = build;
    }

    public String getInstallUrl() {
        return installUrl;
    }

    public void setInstallUrl(String installUrl) {
        this.installUrl = installUrl;
    }

    public String getInstall_url() {
        return install_url;
    }

    public void setInstall_url(String install_url) {
        this.install_url = install_url;
    }

    public String getDirect_install_url() {
        return direct_install_url;
    }

    public void setDirect_install_url(String direct_install_url) {
        this.direct_install_url = direct_install_url;
    }

    public String getUpdate_url() {
        return update_url;
    }

    public void setUpdate_url(String update_url) {
        this.update_url = update_url;
    }

    public BinaryBean getBinary() {
        return binary;
    }

    public void setBinary(BinaryBean binary) {
        this.binary = binary;
    }

    public static class BinaryBean implements  Parcelable{

        private int fsize;

        public int getFsize() {
            return fsize;
        }

        public void setFsize(int fsize) {
            this.fsize = fsize;
        }

        protected BinaryBean(Parcel in) {
            this.fsize = in.readInt();
        }

        public static final Creator<BinaryBean> CREATOR = new Creator<BinaryBean>() {
            @Override
            public BinaryBean createFromParcel(Parcel in) {
                return new BinaryBean(in);
            }

            @Override
            public BinaryBean[] newArray(int size) {
                return new BinaryBean[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.fsize);
        }
    }

    protected UpdateEntity(Parcel in) {
        this.name = in.readString();
        this.build = in.readString();
        this.changelog = in.readString();
        this.install_url = in.readString();
        this.installUrl = in.readString();
        this.direct_install_url = in.readString();
        this.update_url = in.readString();
        this.version = in.readString();
        this.updated_at = in.readString();
        this.binary = in.readParcelable(BinaryBean.class.getClassLoader());

    }

    public static final Creator<UpdateEntity> CREATOR = new Creator<UpdateEntity>() {
        @Override
        public UpdateEntity createFromParcel(Parcel in) {
            return new UpdateEntity(in);
        }

        @Override
        public UpdateEntity[] newArray(int size) {
            return new UpdateEntity[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(this.name);
        dest.writeString(this.build);
        dest.writeString(this.changelog);
        dest.writeString(this.direct_install_url);
        dest.writeString(this.install_url);
        dest.writeString(this.update_url);
        dest.writeString(this.version);
        dest.writeString(this.versionShort);
        dest.writeString(this.updated_at);
        dest.writeString(this.installUrl);
        dest.writeParcelable(this.binary,flags);

    }
}
