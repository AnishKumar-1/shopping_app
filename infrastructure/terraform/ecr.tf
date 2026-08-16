resource "aws_ecr_repository" "eureka" {
  name                 = "shopping-eureka"
  image_tag_mutability = "MUTABLE"
  tags = {
    Name = "shopping-eureka"
  }
}

resource "aws_ecr_repository" "gateway" {
  name = "shopping-gateway"
  image_tag_mutability = "MUTABLE"
  tags = {
    Name="shopping-gateway"
  }
}

resource "aws_ecr_repository" "authentication" {
  name = "shopping-authentication"
  image_tag_mutability = "MUTABLE"
  tags = {
    Name="shopping-authentication"
  }
}

resource "aws_ecr_repository" "product" {
  name = "shopping-product"
  image_tag_mutability = "MUTABLE"
  tags = {
    Name="shopping-product"
  }
}

resource "aws_ecr_repository" "inventory" {
  name = "shopping-inventory"
  image_tag_mutability = "MUTABLE"
  tags = {
    Name="shopping-inventory"
  }
}

resource "aws_ecr_repository" "cart" {
  name = "shopping-cart"
  image_tag_mutability = "MUTABLE"
  tags = {
    Name="shopping-cart"
  }
}

