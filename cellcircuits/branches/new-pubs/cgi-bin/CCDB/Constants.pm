package CCDB::Constants;

require Exporter;

use strict;
use warnings;

our @ISA = qw(Exporter);
our @EXPORT = qw();
our @EXPORT_OK = qw($cgi_version
		    $search_url
		    $cgi_url
		    $data_url
		    $chianti_url
		    $pubCitation 
		    $pubName 
		    $db_link_by_species 
		    $colors
		    $SYNONYM_FILE
		    $DEBUG
		    $DEFAULT_ENRICHMENT_LIMIT
		    $pubSupplementURL
		    $img_format
		    $species_abbrev
		    );
our $VERSION = 1.0;

our $DEBUG = 1;

## limit the number of enrichment retrieved 
## (for model_id based queries only)
our $DEFAULT_ENRICHMENT_LIMIT = 3;  

our $cgi_version  = "v1.0";
our $cgi_url      = "/cgi-bin/search/$cgi_version";

our $search_url   = "";

our $data_url     = "/data";

our $chianti_url = 'http://chianti.ucsd.edu';

our $SYNONYM_FILE = "/opt/www/cgi-bin/search/${cgi_version}/CCDB/synonyms.06182006.tab";

our $pubCitation = {
    'Begley2002_MCR'             => "http://www.ncbi.nlm.nih.gov/entrez/query.fcgi?cmd=Retrieve&db=pubmed&dopt=Abstract&list_uids=12496357&query_hl=3",
    'Bernard2005_PSB'            => "http://www.ncbi.nlm.nih.gov/entrez/query.fcgi?cmd=Retrieve&db=pubmed&dopt=Abstract&list_uids=15759651&query_hl=3",
    'de_Lichtenberg2005_Science' => "http://www.ncbi.nlm.nih.gov/entrez/query.fcgi?cmd=Retrieve&db=pubmed&dopt=Abstract&list_uids=15692050&query_hl=3",
    'Gandhi2006_NG'              => "http://www.ncbi.nlm.nih.gov/entrez/query.fcgi?cmd=Retrieve&db=pubmed&dopt=Abstract&list_uids=16501559&query_hl=3",
    'Hartemink2002_PSB'          => "http://www.ncbi.nlm.nih.gov/entrez/query.fcgi?cmd=Retrieve&db=pubmed&dopt=Abstract&list_uids=11928497&query_hl=3",
    'Haugen2004_GB'              => "http://www.ncbi.nlm.nih.gov/entrez/query.fcgi?cmd=Retrieve&db=pubmed&dopt=Abstract&list_uids=15575969&query_hl=3",
    'Ideker2002_BINF'            => "http://www.ncbi.nlm.nih.gov/entrez/query.fcgi?cmd=Retrieve&db=pubmed&dopt=Abstract&list_uids=12169552&query_hl=3",
    'Kelley2005_NBT'             => "http://www.ncbi.nlm.nih.gov/entrez/query.fcgi?cmd=Retrieve&db=pubmed&dopt=Abstract&list_uids=15877074&query_hl=14",
    'Sharan2005_PNAS'            => "http://www.ncbi.nlm.nih.gov/entrez/query.fcgi?cmd=Retrieve&db=pubmed&dopt=Abstract&list_uids=15687504&query_hl=24",
    'Suthram2005_Nature'         => "http://www.ncbi.nlm.nih.gov/entrez/query.fcgi?cmd=Retrieve&db=pubmed&dopt=Abstract&list_uids=16267557&query_hl=1",
    'Yeang2005_GB'               => "http://www.ncbi.nlm.nih.gov/entrez/query.fcgi?cmd=Retrieve&db=pubmed&dopt=Abstract&list_uids=15998451&query_hl=3",
BandyopadhyayGersten2007     => "http://www.ncbi.nlm.nih.gov/entrez"
};

## Also used as list of default publications by Driver.pm
our $pubName = {
    'Begley2002_MCR'             => "Begley, Molecular Cancer Research (2002)",
    'Bernard2005_PSB'            => "Bernard and Hartemink, Pacific Symposium on Biocomputing (2005)",
    'de_Lichtenberg2005_Science' => "de Lichtenberg, Science (2005)",
    'Gandhi2006_NG'              => "Gandhi, Nataure Genetics (2006)",
    'Hartemink2002_PSB'          => "Hartemink, Pacific Symposium on Biocomputing (2002)",
    'Haugen2004_GB'              => "Haugen, Geneome Biology (2004)",
    'Ideker2002_BINF'            => "Ideker, Bioinformatics (2002)",
    'Kelley2005_NBT'             => "Kelley, Nature Biotechnology (2005)",
    'Sharan2005_PNAS'            => "Sharan, PNAS (2005)",
    'Suthram2005_Nature'         => "Suthram and Sittler, Nature (2005)",
    'Yeang2005_GB'               => "Yeang, Genome Biology (2005)",
    'BandyopadhyayGersten2007'     => "Bandyopadhyay, SUBMITTED (2006)"
};

our $db_link_by_species = {
    'Saccharomyces cerevisiae' => "http://db.yeastgenome.org/cgi-bin/locus.pl?locus="
#    'Caenorhabditis elegans'   => "http://wormbase.org/db/gene/gene?name=",
#    'Drosophila melanogaster'  => "http://flybase.bio.indiana.edu/.bin/fbidq.html?",
#    'Homo sapiens'             => "",
#    'Plasmodium falciparum'    => ""
};

our $colors = {
    page_background => '#ffffff',
    error_message => '#B22222',
    pvalue => '#B22222',
    error_background => '#B4C3CA',
    GO_table_dark  => '#446689',
    GO_table_medium => '#738DA7',
    GO_table_light => '#A2B3C5'
};


our $pubSupplementURL = {
    'de_Lichtenberg2005_Science' => "http://www.cbs.dtu.dk/cellcycle/yeast_complexes/complexes.php",
    'Kelley2005_NBT'             => "$chianti_url/Kelley2005",
    'Sharan2005_PNAS'            => "$chianti_url/Sharan2005",
    'Yeang2005_GB'               => "$chianti_url/Yeang2005"
};


our $img_format = {
    Begley2002_MCR             => "jpg",
    Bernard2005_PSB            => "jpg",
    de_Lichtenberg2005_Science => "jpg",
    Gandhi2006_NG              => "jpg",
    Hartemink2002_PSB          => "jpg",
    Haugen2004_GB              => "jpg",
    Ideker2002_BINF            => "jpg",
    Kelley2005_NBT             => "png",
    Sharan2005_PNAS            => "png",
    Suthram2005_Nature         => "jpg",
    Yeang2005_GB               => "gif",
    BandyopadhyayGersten2007   => "png"
    };

## Also used as the list of default species by Driver.pm
our $species_abbrev = {
    'Caenorhabditis elegans'   => 'C. ele',
    'Drosophila melanogaster'  => 'D. mel',
    'Homo sapiens'             => 'H. sap',
    'Plasmodium falciparum'    => 'P. fal',
    'Saccharomyces cerevisiae' => 'S. cer'
    };


return 1;
