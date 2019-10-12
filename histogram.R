hist_data <- read.table("histogram_tcp_connections.txt", header=FALSE)
hist_data <- as.numeric(unlist(hist_data))
hist(hist_data, main="Histogram of number of TCP flows opened to FTP servers",xlab="Time", ylab="Number of TCP flows", border="blue", col="pink", xlim=c(0,24), las=1, breaks=24)
