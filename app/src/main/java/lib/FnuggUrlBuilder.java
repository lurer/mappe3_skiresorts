package lib;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by espen on 11/24/15.
 */
public class FnuggUrlBuilder {


    private static FnuggUrlBuilder instance;


    private final static String start = "http://fnuggapi.cloudapp.net/search?";
    private final static String sourceFields = "sourceFields=_id,name,location";
    private final static int SIZE = 10;

    private static StringBuilder buildingUrl;

    private static List<String> filters;
    private static List<String> facets;
    private static int page;
    private static boolean facetAdded;


    public FnuggUrlBuilder(){
        buildingUrl = new StringBuilder();
        instance = this;
    }


    public FnuggUrlBuilder startUrl(){

        buildingUrl.append(start);
        return instance;

    }


    public FnuggUrlBuilder sourceFields(){
        buildingUrl.append(sourceFields);

/*        int indexToFind = 0;
        for(int i = buildingUrl.length()-1; i > 2; i--){
            if(buildingUrl.charAt(i) == ':'){
                if(buildingUrl.charAt(i-1) == 'd' && buildingUrl.charAt(i-2) == 'i'){
                    indexToFind = i+1;
                    break;
                }
            }
        }
        buildingUrl = buildingUrl.substring(0, indexToFind);
        buildingUrl = buildingUrl + from + "|" + to;*/
        return instance;
    }




    public FnuggUrlBuilder addRegions(String[] regions){


        if(facets == null)
            facets = new ArrayList<>();

        String region = "region:";

        if(regions != null){

            for(int i = 0; i < regions.length;i++){
                region += regions[i];
                if(i < regions.length-1)
                    region += ",";
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

        buildingUrl.append("&size=10");
        if(page > 0)
            buildingUrl.append("&page=" + page);

        return buildingUrl.toString();
    }


}

