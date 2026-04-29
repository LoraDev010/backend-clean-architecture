output "connection_string" {
  description = "MongoDB Atlas connection string (set as SPRING_DATA_MONGODB_URI)"
  value       = "mongodb+srv://${var.db_username}:${var.db_password}@${mongodbatlas_cluster.franchise.connection_strings[0].standard_srv}/franchisedb?retryWrites=true&w=majority"
  sensitive   = true
}

output "cluster_id" {
  description = "MongoDB Atlas cluster ID"
  value       = mongodbatlas_cluster.franchise.cluster_id
}
