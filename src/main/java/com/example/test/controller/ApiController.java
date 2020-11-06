package com.example.test.controller;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class ApiController {
    @GetMapping("/{tentruyen}/{id}")
    public ResponseEntity<?> getData(@PathVariable("tentruyen") String tentruyen,@PathVariable("id") int id) throws IOException {
        String url = "https://truyenfull.vn/"+tentruyen+"/chuong-"+id+"/";
        Document doc = Jsoup.connect(url).get();
        Element datas = doc.select("div.chapter-c").first();
        Element title = doc.getElementsByClass("truyen-title").first();
        Element chap = doc.getElementsByClass("chapter-title").first();
//        System.out.println(id);

        if(datas == null) {
            return ResponseEntity.ok("truyen bi loi");
        }

        HashMap<String, String> fullData = new HashMap<>();
        fullData.put("title", title.text());

        fullData.put("datas", datas.html());

        fullData.put("chap", chap.text());


        return ResponseEntity.ok(fullData);
//        return ResponseEntity.ok(datas.select(">p").html());
    }
    @GetMapping("/search")
    public ResponseEntity<?> searchData(@RequestParam String name) throws IOException {
        String uri = "https://truyenfull.vn/ajax.php?type=quick_search&str="+name;
        Document doc = Jsoup.connect(uri).get();
        Elements datas = doc.getElementsByClass("list-group-item");
        ArrayList<String> arrayList = new ArrayList<>();
        for ( Element v:datas
             ) {
            arrayList.add(v.text());
        }
        return ResponseEntity.ok(arrayList);
    }

    /**************Trang chu *************/


    //Truyen Hot
    @GetMapping("/home/truyentop")
    public ResponseEntity<?> truyenTop(@RequestParam(value = "id",defaultValue = "all") String id) throws IOException {
        String uri = "https://truyenfull.vn/ajax.php?type=hot_select&id="+id;
        Document doc = Jsoup.connect(uri).get();
        Element body = doc.body();
        Elements datas = body.getElementsByClass("item");

        ArrayList<HashMap<String,String>> arrayList = new ArrayList<>();
        for ( int i = 0 ;i < datas.size();i++
        ) {

                Element v = datas.get(i);
                HashMap<String, String> fullData = new HashMap<>();

                fullData.put("imageLink", v.select("img").attr("src"));

                fullData.put("ten", v.text());

                fullData.put("bookLink", v.select("a").attr("href").replace("https://truyenfull.vn", "https://tse-sus.herokuapp.com/api") + "1");

                arrayList.add(fullData);


        }
        return ResponseEntity.ok(arrayList);
    }

    @GetMapping("/home/newupdate")
    public ResponseEntity<?> newUpdate(@RequestParam(value = "id",required = false,defaultValue = "all") String id) throws IOException {

        String uri = "https://truyenfull.vn/ajax.php?type=new_select&id="+id;
        Document doc = Jsoup.connect(uri).get();
        Elements datas = doc.getElementsByClass("row");
        ArrayList<HashMap<String,String>> arrayList = new ArrayList<>();
        for (int i = 0 ; i < datas.size();i++){
            Element v = datas.get(i);
            HashMap<String, String> fullData = new HashMap<>();
            ArrayList<String> theloai = new ArrayList<>();
            fullData.put("ten", v.select("h3").text());
            fullData.put("date", v.select("div.col-time").text());
            fullData.put("chuong", v.select("div.text-info").text());
            fullData.put("theloai",v.select("div.col-cat").text());
            fullData.put("bookLink", v.select("a").attr("href").replace("https://truyenfull.vn", "https://tse-sus.herokuapp.com/api") + "1");

            arrayList.add(fullData);
        }
        return ResponseEntity.ok(arrayList);
    }

    @GetMapping("/home/truyendone")
    public ResponseEntity<?> truyenDone() throws IOException {
        String uri = "https://truyenfull.vn";
        Document doc = Jsoup.connect(uri).get();
        Elements datas = doc.select("div.list-thumbnail div");
        ArrayList<HashMap<String,String>> arrayList = new ArrayList<>();
        for ( int i = 2 ;i < datas.size();i++
        ) {

            Element v = datas.get(i);
            if(v.select("a div:nth-child(1)").attr("data-desk-image").length() == 0 ){
                continue;
            }
            else {
                HashMap<String, String> fullData = new HashMap<>();

                fullData.put("imageLink", v.select("a div:nth-child(1)").attr("data-desk-image"));

                fullData.put("ten", v.select("a>div>h3").text());

                fullData.put("bookLink", v.select("a").attr("href").replace("https://truyenfull.vn", "https://tse-sus.herokuapp.com/api") + "1");

                arrayList.add(fullData);

            }
        }
        return ResponseEntity.ok(arrayList);
    }
    @GetMapping("/{tentruyen}")
    public ResponseEntity<?> info(@PathVariable("tentruyen") String tentruyen) throws IOException {
        String url = "https://truyenfull.vn/"+tentruyen+"/chuong-"+"/";
        Document doc = Jsoup.connect(url).get();
        Element datas = doc.select("div.chapter-c").first();
        String image = doc.select("div.books > div > img").first().attr("src");
        System.out.println(image);


        HashMap<String, String> fullData = new HashMap<>();
        fullData.put("image", image);
//
//        fullData.put("datas", datas.html());
//
//        fullData.put("chap", chap.text());


        return ResponseEntity.ok(fullData);
//        return ResponseEntity.ok(datas.select(">p").html());
    }
    @GetMapping("/danh-sach/{tenchude}")
    public ResponseEntity<?> listbook(
            @PathVariable("tenchude") String tenchude)
            throws IOException {
        String url = "https://truyenfull.vn/danh-sach/"+tenchude+"/";
        Document doc = Jsoup.connect(url).get();
        Elements datas = doc.getElementsByClass("row");
        ArrayList<HashMap<String,String>> arrayList = new ArrayList<>();
        for ( int i = 1 ;i <=27;i++
        ) {

            Element v = datas.get(i);

                HashMap<String, String> fullData = new HashMap<>();

                fullData.put("imageLink", v.select("div.col-xs-3>div>div").attr("data-desk-image"));

                fullData.put("ten", v.select("h3>a").attr("title"));

                fullData.put("tacgia", v.select("span.author").text());

                fullData.put("chuong", v.select("div.text-info").text());


                arrayList.add(fullData);


        }
        return ResponseEntity.ok(arrayList);

    }
    @GetMapping("/danh-sach/{tenchude}/{page}")
    public ResponseEntity<?> listbookpage(
            @PathVariable("tenchude") String tenchude,
            @PathVariable("page") String page
            )
            throws IOException {
        String url = "https://truyenfull.vn/danh-sach/"+tenchude+"/"+page+"/";
        Document doc = Jsoup.connect(url).get();
        Elements datas = doc.getElementsByClass("row");
        ArrayList<HashMap<String,String>> arrayList = new ArrayList<>();
        for ( int i = 1 ;i <=27;i++
        ) {

            Element v = datas.get(i);

            HashMap<String, String> fullData = new HashMap<>();

            fullData.put("imageLink", v.select("div.col-xs-3>div>div").attr("data-desk-image"));

            fullData.put("ten", v.select("h3>a").attr("title"));

            fullData.put("tacgia", v.select("span.author").text());

            fullData.put("chuong", v.select("div.text-info").text());


            arrayList.add(fullData);


        }
        return ResponseEntity.ok(arrayList);

    }

    //truyen dang hot
    //danh-sach/date?q=ngay/thang/nam

    @GetMapping("/danh-sach/danh-sach-truyen-hot")
    public ResponseEntity<?> truyenDangHot(@RequestParam(value = "type") String type) throws IOException {
        String url = "https://truyenfull.vn/ajax.php?type=top_switch&data_type="+type+"&data_limit=10";
        Document doc = Jsoup.connect(url).get();
        Element docs = doc.body();
        Elements datas = docs.getElementsByClass("row top-item");
        ArrayList<HashMap<String,String>> arrayList = new ArrayList<>();
        for ( int i = 0 ;i < datas.size();i++
        ) {

            Element v = datas.get(i);
            ArrayList<String> theloai = new ArrayList<>();
            HashMap<String, String> fullData = new HashMap<>();
            for(int j  = 0 ; j< v.select("div>a").size();j++){
                theloai.add(v.select("div>a").get(j).text());
            }

            fullData.put("ten", v.select("h3>a").attr("title"));
            fullData.put("theloai",theloai.toString());

            arrayList.add(fullData);


        }
        return ResponseEntity.ok(arrayList);

    }
//    //the loai
//
//    @GetMapping("/the-loai/{tentheloai}")
//    public ResponseEntity<?> theLoai(@PathVariable(value = "tentheloai") String tentheloai) throws IOException {
//        String url = "https://truyenfull.vn/the-loai/"+tentheloai+"/";
//        Document doc = Jsoup.connect(url).get();
//        Elements datas = doc.getElementsByClass("row");
//        ArrayList<HashMap<String,String>> arrayList = new ArrayList<>();
//        for ( int i = 1 ;i <= 27;i++
//        ) {
//
//            Element v = datas.get(i);
//
//            HashMap<String, String> fullData = new HashMap<>();
//
//            fullData.put("imageLink", v.select("div.col-xs-3>div>div").attr("data-desk-image"));
//
//            fullData.put("ten", v.select("h3>a").attr("title"));
//
//            fullData.put("tacgia", v.select("span.author").text());
//
//            fullData.put("chuong", v.select("div.text-info").text());
//
//
//            arrayList.add(fullData);
//
//
//        }
//        return ResponseEntity.ok(arrayList);
//
//    }
    @GetMapping("/the-loai/{tentheloai}/{page}")
    public ResponseEntity<?> theLoaiPage(
            @PathVariable(value = "tentheloai") String tentheloai,
            @PathVariable(value = "page") String page
    ) throws IOException {
        String url = "https://truyenfull.vn/the-loai/" + tentheloai + "/" + page + "/";

        Document doc = Jsoup.connect(url).get();
        Elements datas = doc.getElementsByClass("row");
        ArrayList<HashMap<String,String>> arrayList = new ArrayList<>();
        for ( int i = 1 ;i <= 27;i++
        ) {

            Element v = datas.get(i);

            HashMap<String, String> fullData = new HashMap<>();

            fullData.put("imageLink", v.select("div.col-xs-3>div>div").attr("data-desk-image"));

            fullData.put("ten", v.select("h3>a").attr("title"));

            fullData.put("tacgia", v.select("span.author").text());

            fullData.put("chuong", v.select("div.text-info").text());


            arrayList.add(fullData);


        }
        return ResponseEntity.ok(arrayList);

    }

}
