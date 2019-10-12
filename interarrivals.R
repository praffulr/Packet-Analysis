cdf_data = read.table("interarrivals.txt", header=FALSE)
cdf_data <- as.numeric(unlist(cdf_data))
library("fitdistrplus")
plotdist(cdf_data, histo=TRUE, demp=TRUE)
plot(cdf_data)
descdist(cdf_data)

