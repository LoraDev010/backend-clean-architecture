variable "mongodbatlas_public_key" {
  description = "MongoDB Atlas public API key"
  type        = string
  sensitive   = true
}

variable "mongodbatlas_private_key" {
  description = "MongoDB Atlas private API key"
  type        = string
  sensitive   = true
}

variable "atlas_org_id" {
  description = "MongoDB Atlas organization ID"
  type        = string
}

variable "atlas_project_name" {
  description = "MongoDB Atlas project name"
  type        = string
  default     = "franchise-api"
}

variable "db_username" {
  description = "Database user name"
  type        = string
  default     = "franchise_user"
}

variable "db_password" {
  description = "Database user password"
  type        = string
  sensitive   = true
}

variable "allowed_ip" {
  description = "IP address allowed to connect (use 0.0.0.0/0 for all)"
  type        = string
  default     = "0.0.0.0/0"
}
