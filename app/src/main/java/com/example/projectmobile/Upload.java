package com.example.projectmobile;

public class Upload {
    String mName;
    String mDeskripsi;
    String mImageUrl;
    String mHarga;
    String mLokasi;
    String mTanggal;
    String mTelp;


    public Upload(){
    //empty constructur needed
    }

    public Upload(String name, String deskripsi, String imageUrl, String harga, String lokasi, String tanggal, String telp) {
        this.mName = name;
        this.mDeskripsi = deskripsi;
        this.mImageUrl = imageUrl;
        this.mHarga = harga;
        this.mLokasi = lokasi;
        this.mTanggal = tanggal;
        this.mTelp = telp;
    }



    public String getName(){
        return mName;
    }

    public void  setName(String name){
        mName = name;
    }

    public String getDeskripsi(){
        return mDeskripsi;
    }

    public void  setDeskripsi(String deskripsi){
        mName = deskripsi;
    }

    public String getImageUrl(){
        return mImageUrl;
    }

    public void setImageUrl( String imageUrl)
    {
        mImageUrl = imageUrl;
    }

    public String getHarga(){
        return mHarga;
    }

    public void  setHarga(String harga){
        mName = harga;
    }

    public String getLokasi(){
        return mLokasi;
    }

    public void  setLokasi(String lokasi){
        mName = lokasi;
    }

    public String getTanggal(){
        return mTanggal;
    }

    public void  setTanggal(String tanggal){
        mName = tanggal;
    }

    public String getTelp(){
        return mTelp;
    }

    public void  setTelp(String telp){
        mName = telp;
    }

}
