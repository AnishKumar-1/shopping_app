resource "aws_ecr_repository" "eureka" {
  name                 = "shopping-eureka"
  image_tag_mutability = "MUTABLE"
  tags = {
    Name = "shopping-eureka"
  }
}
