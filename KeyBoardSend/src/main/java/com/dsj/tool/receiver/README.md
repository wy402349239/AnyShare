# 扫描局域网设备

private void scan(){
        P2PManager p2PManager = new P2PManager(getApplicationContext());
        P2PNeighbor melonInfo = new P2PNeighbor();
        melonInfo.alias = alias;
        String ip = null;
        try {
            ip = AccessPointManager.getLocalIpAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        if (TextUtils.isEmpty(ip))
            ip = NetworkUtils.getLocalIp(getApplicationContext());
        melonInfo.ip = ip;

        p2PManager.start(melonInfo, new Melon_Callback() {
            @Override
            public void Melon_Found(P2PNeighbor melon) {
                if (melon != null) {
                    if (!neighbors.contains(melon))
                        neighbors.add(melon);
                }
            }

            @Override
            public void Melon_Removed(P2PNeighbor melon) {
                if (melon != null) {
                    neighbors.remove(melon);
                }
            }
        });
    }
    
    
#发送信息

p2PManager.sendStrMsg(neighbors.get(0), msgStr, new SendFile_Callback() {
            @Override
            public void BeforeSending() {

            }

            @Override
            public void OnSending(P2PFileInfo file, P2PNeighbor dest) {

            }

            @Override
            public void AfterSending(P2PNeighbor dest) {

            }

            @Override
            public void AfterAllSending() {

            }

            @Override
            public void AbortSending(int error, P2PNeighbor dest) {

            }
        });
        
#引用及权限

AndroidManifest:
    <uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
    <uses-permission android:name="android.permission.CHANGE_WIFI_STATE" />
    <uses-permission android:name="android.permission.CHANGE_WIFI_MULTICAST_STATE" />
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
    <uses-permission android:name="android.permission.INTERNET" />
    
    application
    <uses-library
                android:name="org.apache.http.legacy"
                android:required="false" />
                
build
android {
    useLibrary 'org.apache.http.legacy'
}
dependencies {
    implementation 'com.android.support:support-v4:28.0.0'
    implementation 'com.android.support:appcompat-v7:28.0.0'
    implementation 'com.android.support:design:28.0.0'
    implementation 'com.android.support:recyclerview-v7:28.0.0'
    implementation 'com.android.support:cardview-v7:28.0.0'
    implementation 'com.github.bumptech.glide:glide:4.9.0'
}