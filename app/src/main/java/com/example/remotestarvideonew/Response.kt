package com.example.remotestarvideonew

data class VideoResponse(
   var page:Integer? = null,
   var results:ArrayList<Video>? = null
)

data class ErrorBody(val error: Boolean, val message: String)

data class Video(
   val id:Int, val adult:Boolean, val overview:String, val poster_path:String,val release_date:String,val first_air_date:String, val original_title:String, val original_name:String
)