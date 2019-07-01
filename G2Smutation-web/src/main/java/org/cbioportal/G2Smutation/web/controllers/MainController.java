package org.cbioportal.G2Smutation.web.controllers;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.security.auth.message.callback.PrivateKeyCallback.Request;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;


import org.apache.commons.lang3.StringUtils;
import org.cbioportal.G2Smutation.scripts.PdbScriptsPipelineRunCommand;
import org.cbioportal.G2Smutation.web.domain.*;
import org.cbioportal.G2Smutation.web.models.Ensembl;
import org.cbioportal.G2Smutation.web.models.InputAlignment;
import org.cbioportal.G2Smutation.web.models.InputSequence;
import org.cbioportal.G2Smutation.web.models.MutationUsageTableResult;
import org.cbioportal.G2Smutation.web.models.QueryProteinName;
import org.cbioportal.G2Smutation.web.models.Statistics;
import org.cbioportal.G2Smutation.web.models.Uniprot;
import org.cbioportal.G2Smutation.web.models.db.Clinvar;
import org.cbioportal.G2Smutation.web.models.db.Cosmic;
import org.cbioportal.G2Smutation.web.models.db.Dbsnp;
import org.cbioportal.G2Smutation.web.models.db.Genie;
import org.cbioportal.G2Smutation.web.models.db.MutationUsageTable;
import org.cbioportal.G2Smutation.web.models.db.StructureAnnotation;
import org.cbioportal.G2Smutation.web.models.db.Tcga;
import org.cbioportal.G2Smutation.web.models.db.mutation_usage_table;
import org.cbioportal.G2Smutation.web.models.db.pdb_seq_alignment;
import org.cbioportal.G2Smutation.web.models.db.rs_mutation_entry;
import org.cbioportal.G2Smutation.web.models.db.rs_mutation_entry_Initia;
import org.cbioportal.G2Smutation.web.models.mutation.Mutation;
import org.cbioportal.G2Smutation.web.models.mutation.MutationAnnotation;
import org.cbioportal.G2Smutation.web.models.InputResidue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * Main Controller of the whole website Control the input sequence to blast
 * Mainly use InputSequence in model
 * 
 * @author Juexin wang
 * 
 */
@Controller
@RestController
public class MainController {
	
	@Autowired
	private MutationUsageTableRepository mutationUsageTableRepository;
	
	@Autowired
	private MutationRepository mutationRepository;
	// This means to get the bean called userRepository
    // Which is auto-generated by Spring, we will use it to handle the data
	
	@Autowired
	private RsRepository RsRepository;
	// This means to get the bean called userRepository
    // Which is auto-generated by Spring, we will use it to handle the data
	
	@Autowired
	private RsRepositoryInitia RsRepositoryInitia;
	// This means to get the bean called userRepository
    // Which is auto-generated by Spring, we will use it to handle the data
	
	@Autowired
	private PdbRepository PdbRepository;
	// This means to get the bean called userRepository
    // Which is auto-generated by Spring, we will use it to handle the data
	
	
	// This means to get the bean called userRepository
    // Which is auto-generated by Spring, we will use it to handle the data
	
	@Autowired
	private ClinvarRepository clinvarRepository;
	// This means to get the bean called userRepository
    // Which is auto-generated by Spring, we will use it to handle the data
	
	@Autowired
	private CosmicRepository cosmicRepository;
	// This means to get the bean called userRepository
    // Which is auto-generated by Spring, we will use it to handle the data
	
	@Autowired
	private DbsnpRepository dbsnpRepository;
	// This means to get the bean called userRepository
    // Which is auto-generated by Spring, we will use it to handle the data
	
	@Autowired
	private GenieRepository genieRepository;
	// This means to get the bean called userRepository
    // Which is auto-generated by Spring, we will use it to handle the data
	
	@Autowired
	private TcgaRepository tcgaRepository;
	// This means to get the bean called userRepository
    // Which is auto-generated by Spring, we will use it to handle the data
	
    @Autowired
    private StatisticsRepository statisticsRepository;
    
    @Autowired
    private EnsemblRepository ensemblRepository;
    
    @Autowired
    private UniprotRepository uniprotRepository;
    
    @Autowired
	private SeqIdAlignmentController seqController;
    
    @Autowired
    private MainGetMappedProteinMutationController proteinMutationRepository;
    
    @Autowired
    HttpServletRequest request;


    @GetMapping("/sequence")
    public ModelAndView inputForm(Model model) {
        model.addAttribute("inputsequence", new InputSequence());
        return new ModelAndView("sequence");
    }

    @PostMapping("/sequence")
    public ModelAndView resultBack(@ModelAttribute @Valid InputSequence inputsequence, BindingResult bindingResult,
            HttpServletRequest request) {
        if (bindingResult.hasErrors()) {
            return new ModelAndView("sequence");
        }

        // is client behind something?
        String ipAddress = request.getHeader("X-FORWARDED-FOR");
        if (ipAddress == null) {
            ipAddress = request.getRemoteAddr();
        }

        inputsequence.setId(ipAddress);
        // inputsequence.setSequence(inputsequence.getSequence());

        PdbScriptsPipelineRunCommand pdbScriptsPipelineRunCommand = new PdbScriptsPipelineRunCommand();
        List<InputAlignment> alignments = pdbScriptsPipelineRunCommand.runCommand(inputsequence);

        // Instant instant = Instant.now ();
        // inputsequence.setTimenow(instant.toString());	databaseRepository
        inputsequence.setTimenow(LocalDateTime.now().toString().replace("T", " "));

        List<InputResidue> residues = new ArrayList<InputResidue>();
        int inputAA = 0;
        if (inputsequence.getResidueNumList().size() != 0) {
            inputAA = Integer.parseInt(inputsequence.getResidueNumList().get(0));
        }

        for (InputAlignment ali : alignments) {
            // if getResidueNum is empty, then return alignments
            // else, return residues
            if (inputsequence.getResidueNumList().size() == 0
                    || (inputAA >= ali.getSeqFrom() && inputAA <= ali.getSeqTo())) {
                InputResidue re = new InputResidue();
                re.setAlignmentId(ali.getAlignmentId());
                re.setBitscore(ali.getBitscore());
                re.setChain(ali.getChain());
                re.setSeqAlign(ali.getSeqAlign());
                re.setSeqFrom(ali.getSeqFrom());
                re.setSeqId(ali.getSeqId());
                re.setSeqTo(ali.getSeqTo());
                re.setSegStart(ali.getSegStart());
                re.setEvalue(ali.getEvalue());
                re.setIdentity(ali.getIdentity());
                re.setIdentp(ali.getIdentp());
                re.setMidlineAlign(ali.getMidlineAlign());
                re.setPdbAlign(ali.getPdbAlign());
                re.setPdbFrom(ali.getPdbFrom());
                re.setPdbId(ali.getPdbId());
                re.setPdbNo(ali.getPdbNo());
                re.setPdbSeg(ali.getPdbSeg());
                re.setPdbTo(ali.getPdbTo());

                if (!(inputsequence.getResidueNumList().size() == 0)) {
                    re.setResidueName(
                            ali.getPdbAlign().substring(inputAA - ali.getSeqFrom(), inputAA - ali.getSeqFrom() + 1));
                    re.setResidueNum(new Integer(
                            Integer.parseInt(ali.getSegStart()) - 1 + ali.getPdbFrom() + (inputAA - ali.getSeqFrom()))
                                    .toString());
                }
                // For percentage
                int queryLength = ali.getSeqAlign().length();
                int targetLength = ali.getPdbAlign().length();
                int queryGapLength = StringUtils.countMatches(ali.getSeqAlign(), "-");
                int targetGapLength = StringUtils.countMatches(ali.getPdbAlign(), "-");
                int gapLength = Math.abs(queryGapLength - targetGapLength);

                // Test:
                if (queryLength != targetLength) {
                    System.out.println("Error! in " + ali.getPdbNo());
                }

                re.setIdentityPercentage(String.format("%.2f", ali.getIdentity() * 1.0f / queryLength));
                re.setPositivePercentage(String.format("%.2f", ali.getIdentp() * 1.0f / queryLength));
                re.setGapPercentage(String.format("%.2f", gapLength * 1.0f / queryLength));
                re.setGap(gapLength);
                re.setLength(queryLength);
                re.setIdentityPercentageStr("(" + ali.getIdentity() + "/" + queryLength + ")");
                re.setPositivePercentageStr("(" + ali.getIdentp() + "/" + queryLength + ")");
                re.setGapPercentageStr("(" + gapLength + "/" + queryLength + ")");

                // Parameters for output TODO: not optimize
                re.setParaEvalue(inputsequence.getEvalue());
                re.setWord_size(inputsequence.getWord_size());
                re.setGapopen(inputsequence.getGapopen());
                re.setGapextend(inputsequence.getGapextend());
                re.setMatrix(inputsequence.getMatrix());
                re.setComp_based_stats(inputsequence.getComp_based_stats());
                re.setThreshold(inputsequence.getThreshold());
                re.setWindow_size(inputsequence.getWindow_size());

                re.setBlast_dblen(ali.getBlast_dblen());
                re.setBlast_dbnum(ali.getBlast_dbnum());
                re.setBlast_effspace(ali.getBlast_effspace());
                re.setBlast_entropy(ali.getBlast_entropy());
                re.setBlast_hsplen(ali.getBlast_hsplen());
                re.setBlast_kappa(ali.getBlast_kappa());
                re.setBlast_lambda(ali.getBlast_lambda());
                re.setBlast_reference(ali.getBlast_reference());
                re.setBlast_version(ali.getBlast_version());

                re.setTimenow(inputsequence.getTimenow());

                // input
                re.setSequence(inputsequence.getSequence());
                if (inputsequence.getResidueNumList().size() != 0) {
                    re.setInputResidueNum(inputsequence.getResidueNumList().get(0));
                } else {
                    re.setInputResidueNum("");
                }

                residues.add(re);
            }
        }
        return new ModelAndView("/result", "residues", residues);
    }

    // Original Mapping
    @GetMapping("/pageapi")
    public ModelAndView apiInfo() {
        return new ModelAndView("api");
    }

    @GetMapping("/clients")
    public ModelAndView clientsInfo() {
        return new ModelAndView("clients");
    }
    
    @GetMapping("/statistics")
    public ModelAndView statisticsInfo(Model model) {
        List<Statistics> statistics = statisticsRepository.findTop2ByOrderByIdDesc();
        return new ModelAndView("/statistics", "statistics", statistics);
    }
    

    @GetMapping("/about")
    public ModelAndView aboutInfo() {
        return new ModelAndView("about");
    }
    
    @GetMapping("/contact")
    public ModelAndView contactInfo() {
        return new ModelAndView("contact");
    }

    @GetMapping("/")
    public ModelAndView homeInfo(Model model) {
    	model.addAttribute("queryproteinname", new QueryProteinName());
        return new ModelAndView("frontpage");
    }
    
    
    @PostMapping("/")
    public ModelAndView homeInfoBack(@ModelAttribute @Valid QueryProteinName queryproteinname, BindingResult bindingResult,
            HttpServletRequest request) {   	
    	if (bindingResult.hasErrors()) {
            return new ModelAndView("frontpage");
        }
    	return new ModelAndView("/proteinvariants","queryproteinname",queryproteinname);
    }
    
    

    
 
    @GetMapping("/detail")
    public ModelAndView detailInfo() {
    	return new ModelAndView("/detail");
    }
    
    /*
    @GetMapping("/detailAnno")
    public ModelAndView detailInfo() {
    	//api/proteinMutationAnno/uniprot/P53_HUMAN/pdb/2pcx_A
    	List<MutationAnnotation> outList = proteinMutationRepository.postProteinMutationAnnotationByPDB("uniprot","P53_HUMAN","2pcx","A",new ArrayList());
    	return new ModelAndView("/detailAnno","outList",outList);
    }
    */
    
    
    // RS database start
    public Integer rstotaldata = 16290997;
    @GetMapping("/rs")    
    public ModelAndView RSInfo(@RequestParam(value="number",defaultValue = "1") Integer number,Model model){
    	List<rs_mutation_entry_Initia> datapage=RsRepositoryInitia.findTop20ByrsIdAfter(0);
        model.addAttribute("data", datapage);
        model.addAttribute("number",number);
        model.addAttribute("rstotaldata",rstotaldata);
        return new ModelAndView("rs");
    }
    
    // RS_SNP_ID Web Jump
    @RequestMapping(value = "/rsSnpIdWebJump",method = RequestMethod.GET)
    public ModelAndView rsSnpIdWebJump(Model model, HttpServletRequest request){
        String url = request.getParameter("urlInfo");
        String result;
        result="https://www.ncbi.nlm.nih.gov/snp/rs" + url;
        return new ModelAndView(new RedirectView(result));
    }
    
    // RS search
    public List<rs_mutation_entry> rsSearchData;
    public Integer rssearchPerPage = 10;
    public Integer rssearchMaxPage=1;
    public Integer rstotalCount;
    
    @RequestMapping("/rssearch/RSSNPID")  
    public ModelAndView rssearchGetRSSNPID(HttpServletRequest request,Model model){  
        String RSSNPID = request.getParameter("RSSNPID");
        List<rs_mutation_entry> rsdatafind = RsRepository.findByrsSnpId(Integer.parseInt(RSSNPID));
        
        rsSearchData=rsdatafind;
        rstotalCount = rsSearchData.size();
        Integer requestCount = rstotalCount / rssearchPerPage==0?1:rstotalCount / rssearchPerPage+1;
        rssearchMaxPage = Math.max(1, requestCount);
        model.addAttribute("requestCount", rssearchMaxPage);
        
        // First page
        Integer number = 1;
        int toIndex = Math.min(rstotalCount, (0 + 1) * rssearchPerPage);
        List<rs_mutation_entry> subList = rsdatafind.subList(0, toIndex);
        
        model.addAttribute("totalCount", rstotalCount);
        model.addAttribute("number",number);
    	model.addAttribute("data", subList);
        return new ModelAndView("rssearch");	
    }
    
    @RequestMapping("/rssearch/SEQID")  
    public ModelAndView rssearchGetSEQID(HttpServletRequest request,Model model){  
        String SEQID = request.getParameter("SEQID");
        List<rs_mutation_entry> rsdatafind = RsRepository.findByseqId(Integer.parseInt(SEQID));
        
        rsSearchData=rsdatafind;
        rstotalCount = rsSearchData.size();
        Integer requestCount = rstotalCount / rssearchPerPage==0?1:rstotalCount / rssearchPerPage+1;
        rssearchMaxPage = Math.max(1, requestCount);
        model.addAttribute("requestCount", rssearchMaxPage);
        
        // First page
        Integer number = 1;
        int toIndex = Math.min(rstotalCount, (0 + 1) * rssearchPerPage);
        List<rs_mutation_entry> subList = rsdatafind.subList(0, toIndex);
        
        model.addAttribute("totalCount", rstotalCount);
        model.addAttribute("number",number);
    	model.addAttribute("data", subList);
        return new ModelAndView("rssearch");	
    }
    
    @RequestMapping("/rssearch/SEQINDEX")  
    public ModelAndView rssearchGetSEQINDEX(HttpServletRequest request,Model model){  
        String SEQINDEX = request.getParameter("SEQINDEX");
        List<rs_mutation_entry> rsdatafind = RsRepository.findByseqIndex(Integer.parseInt(SEQINDEX));
        
        rsSearchData=rsdatafind;
        rstotalCount = rsSearchData.size();
        Integer requestCount = rstotalCount / rssearchPerPage==0?1:rstotalCount / rssearchPerPage+1;
        rssearchMaxPage = Math.max(1, requestCount);
        model.addAttribute("requestCount", rssearchMaxPage);
        
        // First page
        Integer number = 1;
        int toIndex = Math.min(rstotalCount, (0 + 1) * rssearchPerPage);
        List<rs_mutation_entry> subList = rsdatafind.subList(0, toIndex);
        
        model.addAttribute("totalCount", rstotalCount);
        model.addAttribute("number",number);
    	model.addAttribute("data", subList);
        return new ModelAndView("rssearch");	
    }
    
    @RequestMapping("/rssearch/SEQUNION")  
    public ModelAndView rssearchGetSEQUNION(HttpServletRequest request,Model model){  
    	String SEQID = request.getParameter("SEQID");
    	String SEQINDEX = request.getParameter("SEQINDEX");
        List<rs_mutation_entry> rsdatafind = RsRepository.findByseqIdAndSeqIndex(Integer.parseInt(SEQID),Integer.parseInt(SEQINDEX));
        
        rsSearchData=rsdatafind;
        rstotalCount = rsSearchData.size();
        Integer requestCount = rstotalCount / rssearchPerPage==0?1:rstotalCount / rssearchPerPage+1;
        rssearchMaxPage = Math.max(1, requestCount);
        model.addAttribute("requestCount", rssearchMaxPage);
        
        // First page
        Integer number = 1;
        int toIndex = Math.min(rstotalCount, (0 + 1) * rssearchPerPage);
        List<rs_mutation_entry> subList = rsdatafind.subList(0, toIndex);
        
        model.addAttribute("totalCount", rstotalCount);
        model.addAttribute("number",number);
    	model.addAttribute("data", subList);
        return new ModelAndView("rssearch");	
    }
    
    
    @RequestMapping("/rssearch/PDBNO")  
    public ModelAndView rssearchGetPDBNO(HttpServletRequest request,Model model){  
        String PDBNO = request.getParameter("PDBNO");
        List<rs_mutation_entry> rsdatafind = RsRepository.findBypdbNoStartingWith(PDBNO);
        
        rsSearchData=rsdatafind;
        rstotalCount = rsSearchData.size();
        Integer requestCount = rstotalCount / rssearchPerPage==0?1:rstotalCount / rssearchPerPage+1;
        rssearchMaxPage = Math.max(1, requestCount);
        model.addAttribute("requestCount", rssearchMaxPage);
        
        // First page
        Integer number = 1;
        int toIndex = Math.min(rstotalCount, (0 + 1) * rssearchPerPage);
        List<rs_mutation_entry> subList = rsdatafind.subList(0, toIndex);
        
        model.addAttribute("totalCount", rstotalCount);
        model.addAttribute("number",number);
    	model.addAttribute("data", subList);
        return new ModelAndView("rssearch");	
    }
    
    @RequestMapping("/rssearch/PDBINDEX")  
    public ModelAndView rssearchGetPDBINDEX(HttpServletRequest request,Model model){  
        String PDBINDEX = request.getParameter("PDBINDEX");
        List<rs_mutation_entry> rsdatafind = RsRepository.findBypdbIndex(Integer.parseInt(PDBINDEX));
        
        rsSearchData=rsdatafind;
        rstotalCount = rsSearchData.size();
        Integer requestCount = rstotalCount / rssearchPerPage==0?1:rstotalCount / rssearchPerPage+1;
        rssearchMaxPage = Math.max(1, requestCount);
        model.addAttribute("requestCount", rssearchMaxPage);
        
        // First page
        Integer number = 1;
        int toIndex = Math.min(rstotalCount, (0 + 1) * rssearchPerPage);
        List<rs_mutation_entry> subList = rsdatafind.subList(0, toIndex);
        
        model.addAttribute("totalCount", rstotalCount);
        model.addAttribute("number",number);
    	model.addAttribute("data", subList);
        return new ModelAndView("rssearch");	
    }
    
    @RequestMapping("/rssearchPerPageChange")  
    public ModelAndView rssearchPerPageChangeGet(HttpServletRequest request,Model model){
        String page = request.getParameter("perPage");
        Integer perPage = Integer.valueOf(page);
        rssearchPerPage=perPage;

        Integer requestCount = rstotalCount / rssearchPerPage==0?1:rstotalCount / rssearchPerPage+1;
        rssearchMaxPage = Math.max(1, requestCount);
        model.addAttribute("requestCount", rssearchMaxPage);
        
        // First page
        Integer number = 1;
        int toIndex = Math.min(rstotalCount, (0 + 1) * rssearchPerPage);
        List<rs_mutation_entry> subList = rsSearchData.subList(0, toIndex);
        
        model.addAttribute("totalCount", rstotalCount);
        model.addAttribute("number",number);
    	model.addAttribute("data", subList);
        return new ModelAndView("rssearch");
    }
    
    @RequestMapping("/rssearchPage")  
    public ModelAndView rssearchPageGet(HttpServletRequest request,Model model){
        String page = request.getParameter("number");
        Integer number = Integer.valueOf(page);
        
        if (number>=rssearchMaxPage) number=rssearchMaxPage;
        Integer low = (number-1)*rssearchPerPage;
        Integer high = Math.min(rstotalCount, number * rssearchPerPage);
        
        List<rs_mutation_entry> datapage = rsSearchData.subList(low, high);
        model.addAttribute("data", datapage);
        model.addAttribute("totalCount", rstotalCount);
        model.addAttribute("number",number);
        model.addAttribute("requestCount", rssearchMaxPage);
        return new ModelAndView("rssearch");
    }
    // RS database end
    
    
    // Regular database start
    public Integer totaldata = 1119936;
    @GetMapping("/database")    
    public ModelAndView databaseInfo(@RequestParam(value="number",defaultValue = "1") Integer number,Model model){
    	List<mutation_usage_table> datapage=mutationRepository.findTop10BymutationIdGreaterThan(1);
        model.addAttribute("data", datapage);
        model.addAttribute("number",number);
        model.addAttribute("totaldata", totaldata);
        return new ModelAndView("database");
    }
    
    
    // For search
    public List<mutation_usage_table> searchData;
    public Integer searchPerPage = 10;
    public Integer searchMaxPage=1;
    public Integer totalCount;
    
    @RequestMapping("/search/ProteinName")  
    public ModelAndView searchGetProteinName(HttpServletRequest request,Model model){  
        String ProteinName = request.getParameter("ProteinName");
        List<mutation_usage_table> datafind = mutationRepository.findByseqNameContaining(ProteinName);
        
        searchData=datafind;
        totalCount = searchData.size();
        Integer requestCount = totalCount / searchPerPage==0?1:totalCount / searchPerPage+1;
        searchMaxPage = Math.max(1, requestCount);
        model.addAttribute("requestCount", searchMaxPage);
        
        // First page
        Integer number = 1;
        int toIndex = Math.min(totalCount, (0 + 1) * searchPerPage);
        List<mutation_usage_table> subList = datafind.subList(0, toIndex);
        
        model.addAttribute("totalCount", totalCount);
        model.addAttribute("number",number);
    	model.addAttribute("data", subList);
        return new ModelAndView("databasesearch");	
    }
    
    @RequestMapping("/search/SEQINDEX")  
    public ModelAndView searchGetSEQINDEX(HttpServletRequest request,Model model){  
        String SEQINDEX = request.getParameter("SEQINDEX");
        List<mutation_usage_table> datafind = mutationRepository.findByseqIndex(SEQINDEX);
        
        searchData=datafind;
        totalCount = searchData.size();
        Integer requestCount = totalCount / searchPerPage==0?1:totalCount / searchPerPage+1;
        searchMaxPage = Math.max(1, requestCount);
        model.addAttribute("requestCount", searchMaxPage);
        
        // First page
        Integer number = 1;
        int toIndex = Math.min(totalCount, (0 + 1) * searchPerPage);
        List<mutation_usage_table> subList = datafind.subList(0, toIndex);
        
        model.addAttribute("totalCount", totalCount);
        model.addAttribute("number",number);
    	model.addAttribute("data", subList);
    	
        return new ModelAndView("databasesearch");	
    }
    
    @RequestMapping("/search/PDBINDEX")  
    public ModelAndView searchGetPDBINDEX(HttpServletRequest request,Model model){  
        String PDBINDEX = request.getParameter("PDBINDEX");
        List<mutation_usage_table> datafind = mutationRepository.findBypdbIndex(PDBINDEX);
        
        searchData=datafind;
        totalCount = searchData.size();
        Integer requestCount = totalCount / searchPerPage==0?1:totalCount / searchPerPage+1;
        searchMaxPage = Math.max(1, requestCount);
        model.addAttribute("requestCount", searchMaxPage);
        
        // First page
        Integer number = 1;
        int toIndex = Math.min(totalCount, (0 + 1) * searchPerPage);
        List<mutation_usage_table> subList = datafind.subList(0, toIndex);
        
        model.addAttribute("totalCount", totalCount);
        model.addAttribute("number",number);
    	model.addAttribute("data", subList);
    	
        return new ModelAndView("databasesearch");	
    }
    
    @RequestMapping("/search/DOUBLEINDEX")  
    public ModelAndView searchGetDOUBLEINDEX(HttpServletRequest request,Model model){  
    	String SEQINDEX = request.getParameter("SEQINDEX");
    	String PDBINDEX = request.getParameter("PDBINDEX");
        List<mutation_usage_table> datafind = mutationRepository.findByseqIndexAndPdbIndex(SEQINDEX,PDBINDEX);
        
        searchData=datafind;
        totalCount = searchData.size();
        Integer requestCount = totalCount / searchPerPage==0?1:totalCount / searchPerPage+1;
        searchMaxPage = Math.max(1, requestCount);
        model.addAttribute("requestCount", searchMaxPage);
        
        // First page
        Integer number = 1;
        int toIndex = Math.min(totalCount, (0 + 1) * searchPerPage);
        List<mutation_usage_table> subList = datafind.subList(0, toIndex);
        
        model.addAttribute("totalCount", totalCount);
        model.addAttribute("number",number);
    	model.addAttribute("data", subList);
    	
        return new ModelAndView("databasesearch");	
    }
    
    @RequestMapping("/search/SEQRESIDUE")  
    public ModelAndView searchGetSEQRESIDUE(HttpServletRequest request,Model model){  
        String SEQRESIDUE = request.getParameter("SEQRESIDUE");
        List<mutation_usage_table> datafind = mutationRepository.findByseqResidue(SEQRESIDUE);
        
        searchData=datafind;
        totalCount = searchData.size();
        Integer requestCount = totalCount / searchPerPage==0?1:totalCount / searchPerPage+1;
        searchMaxPage = Math.max(1, requestCount);
        model.addAttribute("requestCount", searchMaxPage);
        
        // First page
        Integer number = 1;
        int toIndex = Math.min(totalCount, (0 + 1) * searchPerPage);
        List<mutation_usage_table> subList = datafind.subList(0, toIndex);
        
        model.addAttribute("totalCount", totalCount);
        model.addAttribute("number",number);
    	model.addAttribute("data", subList);
    	
        return new ModelAndView("databasesearch");	
    }
    
    @RequestMapping("/search/PDBRESIDUE")  
    public ModelAndView searchGetPDBRESIDUE(HttpServletRequest request,Model model){  
        String PDBRESIDUE = request.getParameter("PDBRESIDUE");
        List<mutation_usage_table> datafind = mutationRepository.findBypdbResidue(PDBRESIDUE);
        
        searchData=datafind;
        totalCount = searchData.size();
        Integer requestCount = totalCount / searchPerPage==0?1:totalCount / searchPerPage+1;
        searchMaxPage = Math.max(1, requestCount);
        model.addAttribute("requestCount", searchMaxPage);
        
        // First page
        Integer number = 1;
        int toIndex = Math.min(totalCount, (0 + 1) * searchPerPage);
        List<mutation_usage_table> subList = datafind.subList(0, toIndex);
        
        model.addAttribute("totalCount", totalCount);
        model.addAttribute("number",number);
    	model.addAttribute("data", subList);
    	
        return new ModelAndView("databasesearch");	
    }
    
    @RequestMapping("/search/DOUBLERESIDUE")  
    public ModelAndView searchGetDOUBLERESIDUE(HttpServletRequest request,Model model){  
    	String SEQRESIDUE = request.getParameter("SEQRESIDUE");
    	String PDBRESIDUE = request.getParameter("PDBRESIDUE");
        List<mutation_usage_table> datafind = mutationRepository.findByseqResidueAndPdbResidue(SEQRESIDUE,PDBRESIDUE);
        
        searchData=datafind;
        totalCount = searchData.size();
        Integer requestCount = totalCount / searchPerPage==0?1:totalCount / searchPerPage+1;
        searchMaxPage = Math.max(1, requestCount);
        model.addAttribute("requestCount", searchMaxPage);
        
        // First page
        Integer number = 1;
        int toIndex = Math.min(totalCount, (0 + 1) * searchPerPage);
        List<mutation_usage_table> subList = datafind.subList(0, toIndex);
        
        model.addAttribute("totalCount", totalCount);
        model.addAttribute("number",number);
    	model.addAttribute("data", subList);
    	
        return new ModelAndView("databasesearch");	
    }
    
    @RequestMapping("/search/PDB")  
    public ModelAndView searchGetPDB(HttpServletRequest request,Model model){  
        String PDBNO = request.getParameter("PDB");
        List<mutation_usage_table> datafind = mutationRepository.findBypdbNoStartingWith(PDBNO);
        
        searchData=datafind;
        totalCount = searchData.size();
        Integer requestCount = totalCount / searchPerPage==0?1:totalCount / searchPerPage+1;
        searchMaxPage = Math.max(1, requestCount);
        model.addAttribute("requestCount", searchMaxPage);
        
        // First page
        Integer number = 1;
        int toIndex = Math.min(totalCount, (0 + 1) * searchPerPage);
        List<mutation_usage_table> subList = datafind.subList(0, toIndex);
        
        model.addAttribute("totalCount", totalCount);
        model.addAttribute("number",number);
    	model.addAttribute("data", subList);
    	
        return new ModelAndView("databasesearch");	
    }
    
    @RequestMapping("/searchPerPageChange")  
    public ModelAndView searchPerPageChangeGet(HttpServletRequest request,Model model){
        String page = request.getParameter("perPage");
        Integer perPage = Integer.valueOf(page);
        searchPerPage=perPage;
        
        totalCount = searchData.size();
        Integer requestCount = totalCount / searchPerPage+1;
        searchMaxPage = Math.max(1, requestCount);
        model.addAttribute("requestCount", searchMaxPage);
        model.addAttribute("totalCount", totalCount);
        
        // First page
        Integer number = 1;
        int toIndex = Math.min(totalCount,searchPerPage);
        List<mutation_usage_table> subList = searchData.subList(0, toIndex);
        model.addAttribute("number",number);
        model.addAttribute("data", subList);
        return new ModelAndView("databasesearch");
    }

    @RequestMapping("/searchPage")  
    public ModelAndView searchPageGet(HttpServletRequest request,Model model){
        String page = request.getParameter("number");
        Integer number = Integer.valueOf(page);
        
        if (number>=searchMaxPage) number=searchMaxPage;
        Integer low = (number-1)*searchPerPage;
        Integer high = Math.min(totalCount, number * searchPerPage);
        
        List<mutation_usage_table> datapage = searchData.subList(low, high);
        model.addAttribute("data", datapage);
        
        model.addAttribute("totalCount", totalCount);
        model.addAttribute("number",number);
        model.addAttribute("requestCount", searchMaxPage);
        return new ModelAndView("databasesearch");
    }
    // Regular database end
    
    // AlignmentId Page
    @RequestMapping(value = "/AlignmentIdPage",method = RequestMethod.GET)
    public ModelAndView getAlignmentIdPage(Model model, HttpServletRequest request){
        String AlignmentId = request.getParameter("AlignmentId");
        List<pdb_seq_alignment> alignmentdetails = PdbRepository.findByalignmentId(Integer.parseInt(AlignmentId));
        model.addAttribute("details", alignmentdetails);
        return new ModelAndView("alignmentId");
    }
    
    //TODO
    // Structure Page
    /*
    @RequestMapping(value = "/StructurePage",method = RequestMethod.GET)
    public ModelAndView getStructurePage(Model model, HttpServletRequest request){
    	String MutationId = request.getParameter("MutationId");
        List<StructureAnnotation> structuresdetails = structureRepository.findBymutationId(Integer.parseInt(MutationId));
        model.addAttribute("structures", structuresdetails);
        return new ModelAndView("structure");
    }
    */
    
    // Clinvar Page
    @RequestMapping(value = "/ClinvarPage",method = RequestMethod.GET)
    public ModelAndView getclinvarPage(Model model, HttpServletRequest request){
    	String mutationNo = request.getParameter("MutationId");
        List<Clinvar> clinvarsdetails = clinvarRepository.findByMutationNo(mutationNo);
        model.addAttribute("clinvars", clinvarsdetails);
        return new ModelAndView("clinvar");
    }
    
    // Cosmic Page
    @RequestMapping(value = "/CosmicPage",method = RequestMethod.GET)
    public ModelAndView getcosmicPage(Model model, HttpServletRequest request){
    	String mutationId = request.getParameter("MutationId");
        List<Cosmic> cosmicsdetails = cosmicRepository.findByMutationNo(mutationId);
        model.addAttribute("cosmics", cosmicsdetails);
        return new ModelAndView("cosmic");
    }
    
    // Dbsnp Page
    @RequestMapping(value = "/DbsnpPage",method = RequestMethod.GET)
    public ModelAndView getDbsnpPage(Model model, HttpServletRequest request){
    	String mutationNo = request.getParameter("MutationId");
        List<Dbsnp> dbsnpdetails = dbsnpRepository.findByMutationNo(mutationNo);
        model.addAttribute("dbsnps", dbsnpdetails);
        return new ModelAndView("dbsnp");
    }
    
    // Genie Page
    @RequestMapping(value = "/GeniePage",method = RequestMethod.GET)
    public ModelAndView getGeniePage(Model model, HttpServletRequest request){
    	String mutationNo = request.getParameter("MutationId");
        List<Genie> geniedetails = genieRepository.findByMutationNo(mutationNo);
        model.addAttribute("genies", geniedetails);
        return new ModelAndView("genie");
    }
    
    // Tcga Page
    @RequestMapping(value = "/TcgaPage",method = RequestMethod.GET)
    public ModelAndView getTcgaPage(Model model, HttpServletRequest request){
    	String mutationNo = request.getParameter("MutationId");
        List<Tcga> tcgadetails = tcgaRepository.findByMutationNo(mutationNo);
        model.addAttribute("tcgas", tcgadetails);
        return new ModelAndView("tcga");
    }
    
    
    // Two ways for 3D PDB structure
    @RequestMapping(value = "/3Dmol",method = RequestMethod.GET)
    public ModelAndView indexback(Model model, HttpServletRequest request){
        String padInfo = request.getParameter("pdbInfo");
        String pdb;
        pdb= padInfo.substring(0,4);
        String  chain= padInfo.substring(5,6);
        String resi = padInfo.substring(8);
    	String dataSelect2 = "chain:"+chain;
    	String dataSelect3 = "chain:"+chain+";resi:"+resi;
    	
    	model.addAttribute("dataSelect2", dataSelect2);
    	model.addAttribute("dataSelect3", dataSelect3);
    	model.addAttribute("pdb", pdb);
        model.addAttribute("chain", chain);
    	model.addAttribute("resi", resi);

        return new ModelAndView("3Dmol");
    }
    
    @RequestMapping(value = "/ngl",method = RequestMethod.GET)
    public ModelAndView bglback(Model model, HttpServletRequest request){
        String padInfo = request.getParameter("pdbInfo2");
        String pdb= padInfo.substring(0,4);
        String chain= padInfo.substring(5,6);
        String resi = padInfo.substring(8);
        String loadFile = "rcsb://"+pdb;
        String Selection = resi+":"+chain;
        
        model.addAttribute("loadFile", loadFile);
        model.addAttribute("Selection", Selection);
    	model.addAttribute("pdb", pdb);
        model.addAttribute("chain", chain);
    	model.addAttribute("resi", resi);

        return new ModelAndView("ngl");
    }
    
    // seqNameUrl
    @RequestMapping(value = "/seqNameUrl",method = RequestMethod.GET)
    public ModelAndView urlback(Model model, HttpServletRequest request){
        String url = request.getParameter("urlInfo");
        String result;
        if (url.contains("_")){
        	String uniprotInfo=url.substring(0, url.indexOf('_'));
        	result="https://www.uniprot.org/uniprot/"+uniprotInfo;
        }
        else if (url.contains("ENSG")){
        	String ensemblInfo=url.substring(url.indexOf(' ')+1,url.indexOf(' ')+16);
        	result="https://useast.ensembl.org/Homo_sapiens/Gene/Summary?g="+ensemblInfo;
        }
        else{
        	result="https://www.google.com/";
        }
        return new ModelAndView(new RedirectView(result));
    }
    

}
