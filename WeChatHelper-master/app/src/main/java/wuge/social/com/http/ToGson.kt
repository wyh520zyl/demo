package wuge.social.com.http

import android.util.Log


object ToGson {

    /**
     * 把Gson转换为对象
     */
//    fun gsonToObj(){
//        val json = "{\"uid\":\"110120\",\"username\":\"测试\",\"password\":\"22583\",\"telNumber\":\"13000000000\"}"
//        val gson= Gson()
//        val account :Account2=gson.fromJson(json,Account2::class.java)
//        Log.e("gsonToObj",account.toString())
//    }
//    /**
//     * 把对象转换为Gson
//     */
//    fun objToGson(){
//        val gson= Gson()
//        val account =Account()
//        val accountGson =gson.toJson(account)
//        Log.e("objToGson",accountGson.toString())
//    }
//
//    /**
//     * 把Gson转换为集合
//     */
//    fun gsonToList(){
//        val json = "[{\"uid\":\"110121\",\"username\":\"灵魂\",\"password\":\"22583\",\"telNumber\":\"13000000000\"}]"
//        val gson= Gson()
//        val accountList:List<Account> = gson.fromJson(json,object:TypeToken<List<Account>>(){}.type)
//        Log.e("gsonToList","${accountList.size}")
//    }
//    /**
//     * 把集合转换为Gson
//     */
//    fun listToGson(){
//        val gson= Gson()
//        val accountList = ArrayList<Account>()
//        val account = Account();
//        accountList.add(account)
//        val gsonAccount = gson.toJson(accountList)
//        Log.e("listToGson",gsonAccount.toString())
//    }

    /**
     * 把Gson转user对象
     */
//    fun gsonToUser(){
//        val userModelJson="{\"code\":200,\"data\":{\"game_id\":\"110108\",\"is_online\":\"1\",\"is_im\":\"1\",\"nickname\":\"云海\",\"user_id\":\"11002\",\"adolescent_status\":\"0\",\"birthday\":\"1994-06-14\",\"headImg\":\"https:\\/\\/wugeshejiao.oss-cn-shanghai.aliyuncs.com\\/UserHead\\/110029b92494c9535f5330aba8b58e58049b05623c9e2.jpeg\",\"mch_id\":\"0\",\"szr_id\":\"3110976\",\"signature\":\"哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈\",\"nearby_status\":\"1\",\"pwd\":\"\",\"sex\":\"1\",\"exp\":\"110\",\"levels\":\"3\",\"social_value\":\"10\",\"constellation\":\"双子座\",\"source\":\"wuge\",\"wall_url\":\"https:\\/\\/wugeshejiao.oss-cn-shanghai.aliyuncs.com\\/UserWall\\/11002c698e5626253566f23c953269d77aa1bc285dd96.jpeg\",\"latitude\":\"30.7042060\",\"address\":\"四川省成都市成华区\",\"client_id\":\"1\",\"age\":\"26\",\"idCard\":\"\",\"longitude\":\"104.0967920\",\"gold\":\"2276\",\"charm_value\":\"20\"}}"
//        val gson = Gson()
//        val user = gson.fromJson<Data>(userModelJson,Data::class.java)
//        Log.e("gsonToUser",user.toString())
//
//    }
}