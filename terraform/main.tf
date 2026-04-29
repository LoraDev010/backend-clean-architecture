terraform {
  required_providers {
    mongodbatlas = {
      source  = "mongodb/mongodbatlas"
      version = "~> 1.15"
    }
  }
  required_version = ">= 1.5"
}

provider "mongodbatlas" {
  public_key  = var.mongodbatlas_public_key
  private_key = var.mongodbatlas_private_key
}

resource "mongodbatlas_project" "franchise" {
  name   = var.atlas_project_name
  org_id = var.atlas_org_id
}

resource "mongodbatlas_cluster" "franchise" {
  project_id = mongodbatlas_project.franchise.id
  name       = "franchise-cluster"

  provider_name               = "TENANT"
  backing_provider_name       = "AWS"
  provider_region_name        = "US_EAST_1"
  provider_instance_size_name = "M0"
}

resource "mongodbatlas_database_user" "app" {
  username           = var.db_username
  password           = var.db_password
  project_id         = mongodbatlas_project.franchise.id
  auth_database_name = "admin"

  roles {
    role_name     = "readWrite"
    database_name = "franchisedb"
  }
}

resource "mongodbatlas_project_ip_access_list" "allow" {
  project_id = mongodbatlas_project.franchise.id
  cidr_block = var.allowed_ip
  comment    = "Allow app connections"
}
