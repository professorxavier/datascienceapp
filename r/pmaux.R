pm <- function(file, latitude, altitude, years) {
  
  ac_station<-read.csv(file,header=TRUE,sep=",")
  x<-subset(ac_station,substr(ac_station$Data,7,10) %in% years)
  y <- penman(x$TempMinimaMedia,x$TempMaximaMedia,
              x$VelocidadeVentoMedia, NA,latitude,NA,x$InsolacaoTotal/30,
              x$NebulosidadeMedia,NA,NA,
              x$UmidadeRelativaMedia,NA,NA,altitude)
  
  df <- data.frame(x$Data, y)
  colnames(df) <- c("data", "evp")
  df
  
}

pmNebulUmid <- function(file, latitude, altitude, years) {
  
  ac_station<-read.csv(file,header=TRUE,sep=",")
  ac_cols<-subset(ac_station,select=c("Data","VelocidadeVentoMedia","InsolacaoTotal","NebulosidadeMedia","PressaoMedia","TempMaximaMedia","TempMinimaMedia","UmidadeRelativaMedia"))
  ac_years<-subset(ac_cols,substr(ac_cols$Data,7,10) %in% years)
  x <- penman(ac_years$TempMinimaMedia,ac_years$TempMaximaMedia,
              ac_years$VelocidadeVentoMedia,
              NA,latitude,NA,NA,ac_years$NebulosidadeMedia,NA,NA,
              ac_years$UmidadeRelativaMedia,NA,NA,altitude)
  
  df2 <- data.frame(ac_years$Data, x)
  colnames(df2) <- c("data", "evp")
  df2
  
}

pmInsol <- function(file, latitude, altitude, years) {
  
  ac_station<-read.csv(file,header=TRUE,sep=",")
  ac_cols<-subset(ac_station,select=c("Data","VelocidadeVentoMedia","InsolacaoTotal","NebulosidadeMedia","PressaoMedia","TempMaximaMedia","TempMinimaMedia","UmidadeRelativaMedia"))
  ac_years<-subset(ac_cols,substr(ac_cols$Data,7,10) %in% years)
  x <- penman(ac_years$TempMinimaMedia,ac_years$TempMaximaMedia,
              ac_years$VelocidadeVentoMedia,
              NA,latitude,NA,ac_years$InsolacaoTotal/30,
              NA,NA,NA,NA,NA,NA,altitude)
  
  df2 <- data.frame(ac_years$Data, x)
  colnames(df2) <- c("data", "evp")
  df2
  
}

pmNebul <- function(file, latitude, altitude, years) {
  
  ac_station<-read.csv(file,header=TRUE,sep=",")
  ac_cols<-subset(ac_station,select=c("Data","VelocidadeVentoMedia","InsolacaoTotal","NebulosidadeMedia","PressaoMedia","TempMaximaMedia","TempMinimaMedia","UmidadeRelativaMedia"))
  ac_years<-subset(ac_cols,substr(ac_cols$Data,7,10) %in% years)
  x <- penman(ac_years$TempMinimaMedia,ac_years$TempMaximaMedia,
              ac_years$VelocidadeVentoMedia,
              NA,latitude,NA,NA,ac_years$NebulosidadeMedia,NA,NA,NA,
              NA,NA,altitude)
  
  df2 <- data.frame(ac_years$Data, x)
  colnames(df2) <- c("data", "evp")
  df2
  
}
pmFillZeros <- function(file, latitude, altitude, years) {
  
  ac_station<-read.csv(file,header=TRUE,sep=",")
  ac_cols<-subset(ac_station,select=c("Data","VelocidadeVentoMedia","InsolacaoTotal","NebulosidadeMedia","PressaoMedia","TempMaximaMedia","TempMinimaMedia","UmidadeRelativaMedia"))
  ac_years<-subset(ac_cols,substr(ac_cols$Data,7,10) %in% years)
  ac_years[is.na(ac_years)] <- 0
  if (nrow(ac_years)==sum(is.na(ac_years$InsolacaoTotal))) {
    insol=NA
  }
  else {
    insol <- ac_years$InsolacaoTotal
  }
  
  if (nrow(ac_years)==sum(is.na(ac_years$NebulosidadeMedia))) {
    nebul=NA
  }
  else {
    nebul <- ac_years$NebulosidadeMedia
  }

  if (nrow(ac_years)==sum(is.na(ac_years$VelocidadeVentoMedia))) {
    vento=NA
  }
  else {
    vento <- ac_years$VelocidadeVentoMedia
  }

  if (nrow(ac_years)==sum(is.na(ac_years$UmidadeRelativaMedia))) {
    umid=NA
  }
  else {
    umid <- ac_years$UmidadeRelativaMedia
  }
  x <- penman(ac_years$TempMinimaMedia,ac_years$TempMaximaMedia,vento,
              NA,latitude,NA,insol/30,nebul,NA,NA,umid,NA,NA,altitude)

#  do.call(rbind, Map(data.frame, data=ac_years$Data, evp=x))

df2 <- data.frame(ac_years$Data, x)
colnames(df2) <- c("data", "evp")
df2

}


pmAll <- function(file, latitude, altitude) {
  
  ac_station<-read.csv(file,header=TRUE,sep=",")
  ac_cruzeiro<-subset(ac_station,select=c("Data","VelocidadeVentoMedia","InsolacaoTotal","NebulosidadeMedia","PressaoMedia","TempMaximaMedia","TempMinimaMedia","UmidadeRelativaMedia"))
  ac2<-ac_cruzeiro[!is.na(ac_cruzeiro$VelocidadeVentoMedia) & !is.na(ac_cruzeiro$TempMaximaMedia) & !is.na(ac_cruzeiro$TempMinimaMedia) & !is.na(ac_cruzeiro$UmidadeRelativaMedia) & !is.na(ac_cruzeiro$InsolacaoTotal) & !is.na(ac_cruzeiro$NebulosidadeMedia) & !is.na(ac_cruzeiro$PressaoMedia),]
  x <- penman(ac2$TempMinimaMedia,ac2$TempMaximaMedia,
              ac2$VelocidadeVentoMedia,
              NA,latitude,NA,ac2$InsolacaoTotal/30,
              ac2$NebulosidadeMedia,NA,NA,ac2$UmidadeRelativaMedia,
              ac2$PressaoMedia/10,NA,altitude)
  
  #  do.call(rbind, Map(data.frame, data=ac_years$Data, evp=x))
  
  df2 <- data.frame(ac2$Data, x)
  colnames(df2) <- c("data", "evp")
  df2
  
}