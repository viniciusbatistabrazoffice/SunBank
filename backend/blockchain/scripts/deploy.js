const hre = require("hardhat");

async function main() {
  const [deployer] = await hre.ethers.getSigners();
  const initialSupply = hre.ethers.utils.parseEther("1000000");

  console.log("Deploying SunBraz with account:", deployer.address);

  const SunBraz = await hre.ethers.getContractFactory("SunBraz");
  const sunBraz = await SunBraz.deploy(initialSupply);

  await sunBraz.deployed();

  console.log("SunBraz deployed to:", sunBraz.address);
  console.log(
    "Total supply:",
    hre.ethers.utils.formatEther(await sunBraz.totalSupply()),
    "SBZ"
  );
}

main()
  .then(() => process.exit(0))
  .catch((error) => {
    console.error(error);
    process.exit(1);
  });
