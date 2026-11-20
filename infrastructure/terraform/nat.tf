# Elastic IP for NAT Gateway
# resource "aws_eip" "nat_eip" {
#   domain = "vpc"
#
#   tags = {
#     Name = "shopping-nat-eip"
#   }
# }

# NAT Gateway
# resource "aws_nat_gateway" "shopping_nat" {
#   allocation_id = aws_eip.nat_eip.id
#   subnet_id     = aws_subnet.public_subnet.id
#
#   tags = {
#     Name = "shopping-nat-gateway"
#   }
#   //This tells Terraform to create the Internet Gateway before creating NAT Gateway.
#   depends_on = [
#     aws_internet_gateway.shopping_igw
#   ]
# }