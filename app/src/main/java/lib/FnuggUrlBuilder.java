package lib;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Created by espen on 11/24/15.
 */
public class FnuggUrlBuilder {


    private static FnuggUrlBuilder instance;


    private final static String start = "http://fnuggapi.cloudapp.net/search?";
    private final static String sourceFields = "sourceFields=_id,name,location";
    private static int size = 10;

    private static StringBuilder buildingUrl;

    private static List<String> filters;
    private static List<String> facets;
    private static int page;


    public FnuggUrlBuilder(){

        instance = this;
    }


    public FnuggUrlBuilder startUrl(){
        buildingUrl = new StringBuilder();
        page = 0;
        if(filters != null)
            filters.clear();
        if(facets != null)
            facets.clear();
        buildingUrl.append(start);
        return instance;

    }


    public FnuggUrlBuilder sourceFields(){
        buildingUrl.append(sourceFields);

        return instance;
    }




    public FnuggUrlBuilder addRegions(Set<String> regions){


        if(facets == null)
            facets = new ArrayList<>();

        String region = "region:";
        int counter = 0;
        if(regions != null && regions.size() > 0){
            Iterator<String> i = regions.iterator();
            while(i.hasNext()){
                String reg = i.next();
                switch(reg){
                    case "Sør-Vestlandet":
                        region += "S%C3%B8r-Vestlandet";
                        break;
                    case "Sørlandet":
                        region += "S%C3%B8rlandet";
                        break;
                    case "Østlandet":
                        region += "%C3%98stlandet";
                        break;
                    default:
                        region += reg;
                        break;
                }
                if(counter < regions.size()-1)
                    region += ",";
                counter++;
            }
            facets.add(region);
        }


        return instance;
    }




    public FnuggUrlBuilder addSlopeFilter(int lower, int upper){

        if(filters == null){
            filters = new ArrayList<>();
        }
        filters.add("slopes.count:" + lower + "|" + upper);

        return instance;
    }


    public FnuggUrlBuilder addLiftFilter(int lower, int upper){

        if(filters == null){
            filters = new ArrayList<>();
        }
        filters.add("lifts.count:" + lower + "|" + upper);

        return instance;
    }


    public FnuggUrlBuilder page(int page){
        this.page = page;

        return instance;
    }



    public String build(){


        if(facets != null){
            buildingUrl.append("&facet=");
            for(int i = 0; i < facets.size(); i++){
                buildingUrl.append(facets.get(i));
                if(i < facets.size()-1)
                    buildingUrl.append("&");
            }
        }

        if(filters != null){
            buildingUrl.append("&range=");
            for(int i = 0; i < filters.size(); i++){
                buildingUrl.append(filters.get(i));
                if(i < filters.size()-1)
                    buildingUrl.append("&");
            }
        }

        buildingUrl.append("&size=" + size);
        if(page > 0)
            buildingUrl.append("&page=" + page);

        return buildingUrl.toString();
    }



}

