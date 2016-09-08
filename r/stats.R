statsDataset<-function(file, years) {
  
  df<-read.csv(file)
  dfFiltered<-subset(df,substr(df$Data,7,10) %in% years)
  colMeans(is.na(dfFiltered))
  
}

avgColumn<-function(years) {
  
  path<-"files//datasets/semcabecalho/"
  arqs<-dir(path)
  df2 <- data.frame(x = character(length(arqs)), y = numeric(length(arqs)), stringsAsFactors = FALSE)
  for(i in 1:length(arqs)){
    df <- read.csv(paste(path,arqs[i],sep="")) 
    dfFiltered<-subset(df,substr(df$Data,7,10) %in% years)
    media<-mean(dfFiltered$UmidadeRelativaMedia)
    df2$x[i] <- arqs[i]
    df2$y[i] <- media
    
  }
  colnames(df2) <- c("data", "evp")
  df2
  
}