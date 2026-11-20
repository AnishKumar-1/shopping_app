output "vpc_id" {
  description = "ID of our shopping VPC"
  value       = aws_vpc.shopping_vpc.id
}

output "vpc_cidr" {
  description = "CIDR block of our shopping VPC"
  value       = aws_vpc.shopping_vpc.cidr_block
}