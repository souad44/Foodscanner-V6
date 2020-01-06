package com.nextinnovation.pt.barcodescanner.database;

        import android.content.ContentValues;
        import android.content.Context;
        import android.database.Cursor;
        import android.database.sqlite.SQLiteDatabase;
        import android.database.sqlite.SQLiteOpenHelper;
        import android.text.format.DateFormat;
        import android.webkit.JsPromptResult;

        import com.nextinnovation.pt.barcodescanner.R;
        import com.nextinnovation.pt.barcodescanner.model.JsonProduct;
        import com.nextinnovation.pt.barcodescanner.model.Product;
        import com.nextinnovation.pt.barcodescanner.model.User;

        import java.text.ParseException;
        import java.text.SimpleDateFormat;
        import java.util.ArrayList;
        import java.util.Calendar;
        import java.util.Date;
        import java.util.List;
        import java.util.Locale;


public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String LOG = "DatabaseHelper";
    private static final int DATABASE_VERSION = 8;
    private static final String DATABASE_NAME = "ProductDatabase";
    private static final String TABLE_PRODUCT = "history";
    private static final String TABLE_USERS= "users";
    public static final String KEY_NAME = "product_label";
    public static final String TABLE_FAVORIS = "favoris";

    static String dat ;
    static String brand ;
    static String barcod;


    private static final String CREATE_TABLE_INSTRUCTOR = "create table if not exists "
            + TABLE_PRODUCT
            + " (id integer primary key autoincrement,"
            + " product_label varchar(30)," + " brands varchar(50)," + " barcode varchar(50)," + " imagestring varchar(50)," + " created_at Numeric NOT NULL );";

    public static final String SQL_TABLE_USERS = "create table if not exists "
            + TABLE_USERS
            + " (id integer primary key autoincrement,"
            + " username TEXT," + " email TEXT," + " password TEXT"
            + " );";

    public static final String SQL_TABLE_FAVORIS = "create table if not exists "
            + TABLE_FAVORIS
            + "(id INTEGER PRIMARY KEY, product_code INTEGER, user_id INTEGER);";



    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        // TODO Auto-generated constructor stub
    }


    @Override
    public void onOpen(SQLiteDatabase db) {
        onCreate(db);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(CREATE_TABLE_INSTRUCTOR);
        db.execSQL(SQL_TABLE_USERS);
        db.execSQL(SQL_TABLE_FAVORIS);

    }



    @Override
    public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FAVORIS );
        onCreate(db);

    }

    public void addProduct(JsonProduct product) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        if(product.getProduct().getLabels()!=null){
        values.put("product_label", product.getProduct().getLabels());}
        else {
            values.put("product_label", "Null");

    }
        values.put("brands",product.getProduct().getBrands());
        values.put("barcode",product.getProduct().getCode());
        values.put("imagestring",product.getProduct().getProduct_image());
        values.put("created_at",System.currentTimeMillis());
        if(exist(values.getAsString("product_label"))){
            db.insert(TABLE_PRODUCT, null, values);
            db.close();
        }
        else{
            deleteProduct(product.getProduct());
            addProduct(product);
        }
    }

    public void deleteAllFavoris()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_FAVORIS,null,null);
        db.execSQL("delete from "+ TABLE_FAVORIS);
        db.close();
    }

    public void deleteFavoris(String code)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_FAVORIS+ " WHERE product_code ='"+code+"'");
        //db.execSQL("delete from "+ TABLE_FAVORIS);

    }


    public ArrayList<Product> getData() {
        ArrayList<Product> list = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from " + TABLE_PRODUCT, null);
        if (cursor.moveToFirst()){
            do{
                String data = cursor.getString(cursor.getColumnIndex("product_label"));
                String brands = cursor.getString(cursor.getColumnIndex("brands"));
                String barcode = cursor.getString(cursor.getColumnIndex("barcode"));
               // String imagestring = cursor.getString(cursor.getColumnIndex("imagestring"));



                list.add(new Product(data,brands,barcode));
                // do what ever you want here
            }while(cursor.moveToNext());
        }
        cursor.close();
        return list;
    }



    public boolean exist(String label){
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor c = db.rawQuery("SELECT * FROM history WHERE product_label = '"+label+"'", null);
        int count = c.getCount();
        return count <= 0;
    }

    public Cursor getDate(Product produit){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select created_at as created_at from " + TABLE_PRODUCT, null);
        return res;
    }



    public String getFormattedDate(long smsTimeInMilis) {

        Calendar smsTime = Calendar.getInstance();
        smsTime.setTimeInMillis(smsTimeInMilis);

        Calendar now = Calendar.getInstance();

        final String timeFormatString = "h:mm aa";
        final String dateTimeFormatString = "EEEE, MMMM d, h:mm aa";
        final long HOURS = 60 * 60 * 60;
        if (now.get(Calendar.DATE) == smsTime.get(Calendar.DATE) ) {
            return "Today " + DateFormat.format(timeFormatString, smsTime);
        } else if (now.get(Calendar.DATE) - smsTime.get(Calendar.DATE) == 1  ){
            return "Yesterday " + DateFormat.format(timeFormatString, smsTime);
        } else if (now.get(Calendar.YEAR) == smsTime.get(Calendar.YEAR)) {
            return DateFormat.format(dateTimeFormatString, smsTime).toString();
        } else {
            return DateFormat.format("MMMM dd yyyy, h:mm aa", smsTime).toString();
        }
    }


    public Cursor getAllData() {

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_PRODUCT, null);
        return res;
    }
    public void deleteAll()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PRODUCT,null,null);
        db.execSQL("delete from "+ TABLE_PRODUCT);

        db.close();
    }


    public void deleteProduct(Product product) {
        SQLiteDatabase db = this.getWritableDatabase();
        String product_name = product.getLabels();
        db.execSQL("DELETE FROM " + TABLE_PRODUCT+ " WHERE "+KEY_NAME+"='"+product_name+"'");

        //db.delete(TABLE_PRODUCT, KEY_NAME + "=" + product_name, null);
    }
    String table = "history";
    String whereClause = "id";

    public User Authenticate(User user) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS,// Selecting Table
                new String[]{"id", "username", "email", "password"},//Selecting columns want to query
                "email" + "=?",
                new String[]{user.email},//Where clause
                null, null, null);

        if (cursor != null && cursor.moveToFirst() && cursor.getCount() > 0) {
            //if cursor has value then in user database there is user associated with this given email
            User user1 = new User(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3));

            //Match both passwords check they are same or not
            if (user.password.equalsIgnoreCase(user1.password)) {
                return user1;
            }
        }
        //if user password does not matches or there is no record with that email then return @false
        return null;
    }

    public void addUser(User user) {

        //get writable database
        SQLiteDatabase db = this.getWritableDatabase();

        //create content values to insert
        ContentValues values = new ContentValues();

        //Put username in  @values
        values.put("username", user.userName);

        //Put email in  @values
        values.put("email", user.email);

        //Put password in  @values
        values.put("password", user.password);

        // insert row
        long todo_id = db.insert(TABLE_USERS, null, values);
    }

    public boolean isEmailExists(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS,// Selecting Table
                new String[]{"id", "username", "email", "password"},//Selecting columns want to query
                "email" + "=?",
                new String[]{email},//Where clause
                null, null, null);

        if (cursor != null && cursor.moveToFirst()&& cursor.getCount()>0) {
            //if cursor has value then in user database there is user associated with this given email so return true
            return true;
        }

        //if email does not exist return false
        return false;
    }


    public boolean insertContact  (String task, String dateStr)
    {
        Date date;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("task", task);
        db.insert(TABLE_FAVORIS, null, contentValues);
        return true;
    }

    public boolean updateContact (String id, String task, String dateStr)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("task", task);

        db.update(TABLE_FAVORIS, contentValues, "id = ? ", new String[] { id } );
        return true;
    }

    public String getData1(String label) {

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor =db.rawQuery("SELECT * FROM history WHERE product_label = '"+label+"'", null);

        if (cursor.moveToFirst()){
            do{
                dat = cursor.getString(cursor.getColumnIndex("product_label"));
                brand = cursor.getString(cursor.getColumnIndex("brands"));
                barcod = cursor.getString(cursor.getColumnIndex("barcode"));

                // do what ever you want here
            }while(cursor.moveToNext());
        }
        //  cursor.close();
        return  barcod;
    }

    public Cursor getFavorisData(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery("select * from "+TABLE_FAVORIS+" order by id desc", null);
        return res;

    }

    public Cursor getDataSpecific(String id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery("select * from "+TABLE_FAVORIS+" WHERE id = '"+id+"' order by id desc", null);
        return res;

    }

    public boolean insertFavoris  (String user_id, String product_id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("user_id", user_id);
        contentValues.put("product_code", product_id);
        db.insert(TABLE_FAVORIS, null, contentValues);
        return true;
    }


    public Cursor getFavorisByUserId(String id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery("select * from "+TABLE_FAVORIS+" WHERE user_id = '"+id+"' order by id desc", null);
        return res;

    }

    public Cursor getFavorisByCodeAndUserId(String code,String id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery("select * from "+TABLE_FAVORIS+" WHERE product_code = '"+code+"' AND user_id = '"+id+"'", null);
        return res;

    }



}
/*
    public ArrayList<JsonProduct> getAllProduct() {
        ArrayList<JsonProduct> productArrayList = new ArrayList<JsonProduct>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_PRODUCT+" ORDER BY id DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                JsonProduct product = new JsonProduct();
                product.getProduct().setLabels(cursor.getString(1));
                // Adding contact to list
                productArrayList.add(product);
            } while (cursor.moveToNext());
        }

        // return contact list
        return productArrayList;
    }
    */



// Getting contacts Count