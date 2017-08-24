package by.lykashenko.demon.mirparfumanew.Adapters;


import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by demon on 24.05.2017.
 */

public class ParfumCollection implements Parcelable {

    String idParfum;
    String nameParfum;
    String imageParfum;
    Integer reatingParfum;
    String cenaParfum;
    String cenaFor;

    public ParfumCollection() {
    }


    public String getCenaFor() {
        return cenaFor;
    }

    public void setCenaFor(String cenaFor) {
        this.cenaFor = cenaFor;
    }

    public String getNameParfum() {
        return nameParfum;
    }

    public void setNameParfum(String nameParfum) {
        this.nameParfum = nameParfum;
    }

    public String getImageParfum() {
        return imageParfum;
    }

    public void setImageParfum(String imageParfum) {
        this.imageParfum = imageParfum;
    }

    public Integer getReatingParfum() {
        return reatingParfum;
    }

    public void setReatingParfum(Integer reatingParfum) {
        this.reatingParfum = reatingParfum;
    }

    public String getCenaParfum() {
        return cenaParfum;
    }

    public void setCenaParfum(String cenaParfum) {
        this.cenaParfum = cenaParfum;
    }




    public String getIdParfum() {
        return idParfum;
    }

    public void setIdParfum(String idParfum) {
        this.idParfum = idParfum;
    }



    protected ParfumCollection(Parcel in) {
        idParfum = in.readString();
        nameParfum = in.readString();
        imageParfum = in.readString();
        reatingParfum = in.readByte() == 0x00 ? null : in.readInt();
        cenaParfum = in.readString();
        cenaFor = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(idParfum);
        dest.writeString(nameParfum);
        dest.writeString(imageParfum);
        if (reatingParfum == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(reatingParfum);
        }
        dest.writeString(cenaParfum);
        dest.writeString(cenaFor);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<ParfumCollection> CREATOR = new Parcelable.Creator<ParfumCollection>() {
        @Override
        public ParfumCollection createFromParcel(Parcel in) {
            return new ParfumCollection(in);
        }

        @Override
        public ParfumCollection[] newArray(int size) {
            return new ParfumCollection[size];
        }
    };
}