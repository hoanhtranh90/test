package com.example.test.controller;


import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.*;

@RequestMapping("/api/qldt/")
@RestController
@CrossOrigin
public class CrawlQldt {





    @PostMapping("/login")
    public ResponseEntity<?> saveCookies(@RequestParam(value = "user") String username,
                                         @RequestParam(value = "pass") String password

    ) throws IOException {

        String url = "http://qldt.ptit.edu.vn/";
        String url1 = "http://qldt.ptit.edu.vn/default.aspx";
        Connection.Response response2 = Jsoup.connect(url)
                .method(Connection.Method.GET)
                .execute();

        Document responseDocument = response2.parse();

        //        Element eventValidation = responseDocument.select("input[name=__EVENTVALIDATION]").first();
        Element viewState = responseDocument.select("input[name=__VIEWSTATE]").first();
        //        Element viewStateGen = responseDocument.select("input[name=__VIEWSTATEGENERATOR]").first();
//        System.out.println(viewState.attr("value"));
        Connection.Response response = Jsoup.connect(url1)
                .method(Connection.Method.POST)
                .data("ctl00$ContentPlaceHolder1$ctl00$ucDangNhap$txtTaiKhoa", username)
                .data("ctl00$ContentPlaceHolder1$ctl00$ucDangNhap$txtMatKhau", password)
                .data("ctl00$ContentPlaceHolder1$ctl00$ucDangNhap$btnDangNhap", "Đăng Nhập")
                .data("__EVENTTARGET", "")
                .data("__EVENTARGUMENT", "")
                .execute();
        Map<String, String> cookies = response.cookies();

        Document homePage = Jsoup.connect("http://qldt.ptit.edu.vn/Default.aspx?page=thoikhoabieu")
                .cookies(cookies)
                .get();
        Element docs = homePage.getElementById("ctl00_ContentPlaceHolder1_ctl00_pnlTuan");
        Elements sizeKip = docs.select("tbody").first().children();
        TreeMap<String,Map<String,String>> listAll = new TreeMap<>(); //treemap tứ sắp xếp
        for (int i = 2 ; i < 7;i++) { // thứ trong tuần
            TreeMap<String,String> kipTrongNgay = new TreeMap<>();
            String monTrongTiet = String.format("td:nth-child(%d)",i);
            ArrayList<String> monHocTrongNgay = new ArrayList<>();
            for(int j = 0 ; j<=11;j++){
                if(j%2 == 0)
                monHocTrongNgay.add(sizeKip.select(monTrongTiet).get(j).text());
            }
            for(int j = 1;j<=6;j++){
                kipTrongNgay.put(String.valueOf(j),monHocTrongNgay.get(j-1));
            }
            listAll.put(String.valueOf(i), kipTrongNgay);
        }
        Map<String, Map<String, Map<String, String>>> listDayInWeek = new HashMap<>();
        listDayInWeek.put("tkb",listAll);

//        Element docs = homePage.getElementById("ctl00_ContentPlaceHolder1_ctl00_pnlTuan");
//        Elements sizeKip = docs.select("tbody").first().children();
//        List<List<List<TreeMap<String,String>>>> listAll = new ArrayList<>(); //treemap tứ sắp xếp
//        for (int i = 2 ; i < 7;i++) { // thứ trong tuần
//            List<TreeMap<String,String>> kipTrongNgay = new ArrayList<>();
//            String monTrongTiet = String.format("td:nth-child(%d)",i);
//            ArrayList<String> monHocTrongNgay = new ArrayList<>();
//            for(int j = 0 ; j<=11;j++){
//                if(j%2 == 0)
//                    monHocTrongNgay.add(sizeKip.select(monTrongTiet).get(j).text());
//            }
//            for(int j = 1;j<=6;j++){
//                TreeMap<String,String> datatest = new TreeMap<>();
//                datatest.put("kíp"+j,monHocTrongNgay.get(j-1));
//                kipTrongNgay.add(datatest);
//            }
////            kipTrongNgay.add(datatest);
//
//            List<List<TreeMap<String, String>>> dataTamthoi = new ArrayList<>();
//            dataTamthoi.add(kipTrongNgay);
//            listAll.add(dataTamthoi);
//        }
////        Map<String, Map<String, Map<String, String>>> listDayInWeek = new HashMap<>();
//        Map<String, List<List<List<TreeMap<String, String>>>>> listDayInWeek = new HashMap<>();
//        listDayInWeek.put("tkb",listAll);


//
//        Element docs = homePage.getElementById("ctl00_ContentPlaceHolder1_ctl00_pnlTuan");
//        Elements sizeKip = docs.select("tbody").first().children();
//        List<TreeMap<String,List<TreeMap<String,String>>>> listAll = new ArrayList<>(); //treemap tứ sắp xếp
//        for (int i = 2 ; i < 7;i++) { // thứ trong tuần
//            List<TreeMap<String,String>> kipTrongNgay = new ArrayList<>();
//            String monTrongTiet = String.format("td:nth-child(%d)",i);
//            ArrayList<String> monHocTrongNgay = new ArrayList<>();
//            for(int j = 0 ; j<=11;j++){
//                if(j%2 == 0)
//                    monHocTrongNgay.add(sizeKip.select(monTrongTiet).get(j).text());
//            }
//            for(int j = 1;j<=6;j++){
//                TreeMap<String,String> tess = new TreeMap<>();
//                tess.put(String.valueOf(j),monHocTrongNgay.get(j-1));
//                kipTrongNgay.add(tess);
//            }
//            TreeMap<String,List<TreeMap<String,String>>> dax = new TreeMap<>();
//            dax.put(String.valueOf(i), kipTrongNgay);
//            listAll.add(dax);
//        }
//        Map<String, List<TreeMap<String, List<TreeMap<String, String>>>>> listDayInWeek = new HashMap<>();
//        listDayInWeek.put("tkb",listAll);

        return ResponseEntity.ok(listDayInWeek);
    }

    }
